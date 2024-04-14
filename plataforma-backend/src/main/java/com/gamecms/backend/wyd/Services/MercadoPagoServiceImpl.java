package com.gamecms.backend.wyd.Services;


import com.gamecms.backend.wyd.Adapters.ShopInternalToMercadoPagoCartAdapter;
import com.gamecms.backend.wyd.DTO.CreatePreferenceRequestDTO;
import com.gamecms.backend.wyd.DTO.MercadoPagoPaymentsSearchResponseDTO;
import com.gamecms.backend.wyd.DTO.SendDonateToIntegrationShopProductDTO;
import com.gamecms.backend.wyd.Exceptions.*;
import com.gamecms.backend.wyd.Models.PaymentMethod;
import com.gamecms.backend.wyd.Models.Purchase;
import com.gamecms.backend.wyd.Models.ServerCustomer;
import com.gamecms.backend.wyd.Repositories.*;
import com.google.gson.Gson;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class MercadoPagoServiceImpl {

    private final PurchaseRepository purchaseRepository;

    private final PaymentMethodRepository paymentMethodRepository;

    private final PreferenceRepository preferenceRepository;

    private final ServerCustomerRepository customerRepository;

    private final ShopRepository shopRepository;

    @Value("${mercadopagocallback}")
    private String notificationUrlBase;

    private final Gson gson;

    private final String PAYMENT_METHOD_NAME = "MERCADOPAGO";

    private void setAccessToken(String accessToken) {
        MercadoPagoConfig.setAccessToken(accessToken);
    }

    public Preference createPreference(CreatePreferenceRequestDTO requestDTO, Long customerId) {
        ServerCustomer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerServerNaoEncontradoException("saveInternalPreference - Server não encontrado: " + customerId));
        PaymentMethod method = paymentMethodRepository.findByCustomerAndNameEquals(customer, PAYMENT_METHOD_NAME).orElseThrow(() -> new PaymentMethodNaoEncontradoException("saveInternalPreference - Metodo nao encontrado"));
        setAccessToken(method.getPrivateKey());

        PreferencePaymentMethodsRequest methods = PreferencePaymentMethodsRequest.builder()
                .installments(1)
                .defaultInstallments(1)
                .defaultPaymentMethodId("pix")
                .build();

        String externalReference = UUID.randomUUID().toString();
        List<PreferenceItemRequest> mercadoCart = new ShopInternalToMercadoPagoCartAdapter(requestDTO.getCart()).adapt();

        notificationUrlBase = notificationUrlBase.replace("{customerId}", customerId.toString());
        PreferenceRequest request = PreferenceRequest.builder()
                .externalReference(externalReference)
                .items(mercadoCart)
                .paymentMethods(methods)
                .notificationUrl(notificationUrlBase + externalReference)
                .backUrls(
                        PreferenceBackUrlsRequest.builder().success(customer.getDomainFrontEnd() + "/painel?&tab=minhascompras&action=purchase&status=success").build()
                )
                .build();

        try {
            Preference mercadopagoPreference = new PreferenceClient().create(request);
            saveInternalPreference(request.getItems(), requestDTO, mercadopagoPreference, mercadoCart, customerId);
            log.info("Preference Criada - preference: " + mercadopagoPreference.getExternalReference() + " customer: " + customerId);
            return mercadopagoPreference;
        } catch (MPException e) {
            throw new NaoFoiPossivelConectarComMercadoPagoException("Não foi possível conectar com o Mercado Pago, erro: " + e.getMessage());
        } catch (MPApiException e) {
            throw new NaoFoiPossivelConectarComMercadoPagoException("Não foi possível conectar com o Mercado Pago, erro: " + e.getMessage() + ", API Response: " + e.getApiResponse().getContent() + ".");
        }
    }

    private void saveInternalPreference(List<PreferenceItemRequest> cart, CreatePreferenceRequestDTO requestDTO, Preference mercadopagoPreference, List<PreferenceItemRequest> mercadoCart, Long customerId) throws DadosInconsistentesMercadoPagoException {
        ServerCustomer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerServerNaoEncontradoException("saveInternalPreference - Server não encontrado: " + customerId));
        PaymentMethod method = paymentMethodRepository.findByCustomerAndNameEquals(customer, PAYMENT_METHOD_NAME).orElseThrow(() -> new PaymentMethodNaoEncontradoException("saveInternalPreference - Metodo nao encontrado"));
        setAccessToken(method.getPrivateKey());

        if (!cart.equals(mercadoCart)) {
            throw new DadosInconsistentesMercadoPagoException("Não foi possível conectar com o Mercado Pago, erro: dados inconsistentes.");
        }

        BigDecimal somaTotal = cart.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        List<SendDonateToIntegrationShopProductDTO> products = cart.stream()
                .filter(item -> item.getQuantity() >= 1)
                .flatMap(item -> IntStream.range(0, item.getQuantity())
                        .mapToObj(i -> shopRepository.findById(Long.valueOf(item.getId()))
                                .map(shopProduct -> SendDonateToIntegrationShopProductDTO.builder()
                                        .id(shopProduct.getId())
                                        .title(shopProduct.getTitle())
                                        .description(shopProduct.getDescription())
                                        .value(shopProduct.getValue())
                                        .category(shopProduct.getCategory())
                                        .donateMultiplier(shopProduct.getDonateMultiplier())
                                        .build())
                                .orElse(null)))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());


        com.gamecms.backend.wyd.Models.Preference preference = com.gamecms.backend.wyd.Models.Preference
                .builder()
                .statusDetail("created")
                .mercadoPagoPreferenceId(mercadopagoPreference.getId())
                .customer(customer)
                .value(somaTotal)
                .username(requestDTO.getUsername())
                .externalReference(mercadopagoPreference.getExternalReference())
                .mercadoPagoReferenceCart(new Gson().toJson(cart))
                .webShopReferenceCart(new Gson().toJson(products))
                .paid(false)
                .dateInc(Date.from(Instant.now()))
                .importedByCustomer(false)
                .build();

        preferenceRepository.save(preference);


        Purchase purchase = Purchase
                .builder()
                .importedByServerCustomer(false)
                .referenceInPaymentMethod(mercadopagoPreference.getExternalReference())
                .method(method)
                .paid(false)
                .customer(customer)
                .username(requestDTO.getUsername())
                .webShopReferenceCart(new Gson().toJson(products))
                .value(somaTotal)
                .build();

        purchase.setDateInc(Date.from(Instant.now()));

        purchaseRepository.save(purchase);
    }

    public void updatePreference(String externalReference, Long customerId, HttpServletRequest servletRequest) {
        ServerCustomer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerServerNaoEncontradoException("saveInternalPreference - Server não encontrado: " + customerId));
        PaymentMethod method = paymentMethodRepository.findByCustomerAndNameEquals(customer, PAYMENT_METHOD_NAME).orElseThrow(() -> new PaymentMethodNaoEncontradoException("saveInternalPreference - Metodo nao encontrado"));
        setAccessToken(method.getPrivateKey());

        try {

            String uri = servletRequest.getRequestURI();
            String pathInfo = servletRequest.getPathInfo();
            String bodyInfo = Collections.list(servletRequest.getParameterNames())
                    .stream()
                    .map(param -> param + ": " + servletRequest.getParameter(param))
                    .reduce("", (acc, curr) -> acc + ", " + curr);


            String logVerificacao = String.format("Verificando Callback - URI: %s, Path Info: %s, Body: {%s}", uri, pathInfo, bodyInfo);

            log.info(logVerificacao);

            boolean processById = servletRequest.getParameter("type") != null && servletRequest.getParameter("type").equals("payment");

            if (processById) {
                String paymentId = servletRequest.getParameter("data.id");
                if (paymentId == null) {
                    log.info("Nao foi possível localizar data.id, reference: " + externalReference + ", skipando purchase.");
                    return;
                }

                OkHttpClient httpClient = new OkHttpClient();

                HttpUrl url = HttpUrl.parse("https://api.mercadopago.com/v1/payments/search")
                        .newBuilder()
                        .addQueryParameter("id", paymentId)
                        .build();

                Headers headers = Headers.of(
                        "Authorization", "Bearer " + MercadoPagoConfig.getAccessToken()
                );

                Request request = new Request.Builder()
                        .url(url.toString())
                        .headers(headers)
                        .build();

                Response response = httpClient.newCall(request).execute();
                String responseBody = response.body().string();
                MercadoPagoPaymentsSearchResponseDTO responseDTO = gson.fromJson(responseBody, MercadoPagoPaymentsSearchResponseDTO.class);

                if (responseDTO.results.isEmpty()) {
                    log.info("**** IMPORT DONATE NAO PAGO **** Ainda não foi processado (PAGO) - preference: " + externalReference + " - customerid: " + customerId + " - response: " + responseBody);
                    return;
                }


                MercadoPagoPaymentsSearchResponseDTO.Payment simplifiedPayment = responseDTO.results.get(0);
                com.gamecms.backend.wyd.Models.Preference internalPreference = preferenceRepository.findByExternalReferenceAndCustomerId(externalReference, customerId);

                if (internalPreference.isImportedByCustomer()) {
                    log.error("****  IMPORT DONATE **** - DONATE JA IMPORTADO, customer: " + customerId + " externalReference: " + externalReference);
                    return;
                }

                boolean isPaid = simplifiedPayment.status_detail.equals("accredited");
                if (isPaid) {
                    internalPreference.setPaid(true);
                    internalPreference.setStatusDetail(simplifiedPayment.status_detail);
                    preferenceRepository.save(internalPreference);

                    Purchase purchase = purchaseRepository.findByCustomerIdAndReferenceInPaymentMethodEquals(customerId, internalPreference.getExternalReference())
                            .orElseThrow(() -> new PurchaseNaoEncontradoException("updatePreference - Compra não encontrada - " + internalPreference.getExternalReference()));
                    purchase.setPaid(true);

                    purchaseRepository.save(purchase);

                    log.warn("****  IMPORT DONATE **** CALLBACK !! PREFERENCE PAGA !! -, disponibilizada no JOB para ser importada, reference: " + externalReference + " - status de pago: " + simplifiedPayment.status_detail);
                }
            }


        } catch (Exception e) {
            log.error("Callback - Não foi possível atualizar a referência: " + externalReference);
            throw new NaoFoiPossivelConectarComMercadoPagoException("Não foi possível conectar com o Mercado Pago, erro: " + e.getMessage());
        }
    }
}
