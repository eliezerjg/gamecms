package com.gamecms.backend.wyd.Jobs;


import com.gamecms.backend.wyd.DTO.DiscordRequestDTO;
import com.gamecms.backend.wyd.Models.Purchase;
import com.gamecms.backend.wyd.Models.ServerCustomer;
import com.gamecms.backend.wyd.Repositories.PurchaseRepository;
import com.gamecms.backend.wyd.Repositories.ServerCustomerRepository;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
@Slf4j
@RequiredArgsConstructor
public class PurchaseItemsSenderJob {

    private final PurchaseRepository purchaseRepository;
    private final ServerCustomerRepository serverCustomerRepository;
    private final Gson gson;

    @PostConstruct
    private void init() {
        runJob();
    }

    private void runJob(){
        TimerTask task = new TimerTask() {
            public void run() {
                log.info("****  IMPORT DONATE JOB  **** - Rodando Job PreferenceItemsSenderJob ");
                importDonate();
            }
        };
        Timer timer = new Timer("Timer job PreferenceItemsSenderJob");
        long jobDelay = 15000L;
        timer.scheduleAtFixedRate(task, 0, jobDelay);
    }

    private void importDonate(){
        List<ServerCustomer> clientes = serverCustomerRepository.findAll();
        log.info("****  IMPORT DONATE JOB  **** - Clientes:  " + clientes.size());
        clientes.forEach(cliente -> {
            List<Purchase> purchaseList = purchaseRepository.findAllByCustomerIdAndImportedByServerCustomerIsFalseAndPaidIsTrue(cliente.getId());
                    log.info("****  IMPORT DONATE JOB  **** - Customer Id:  " + cliente.getId() + " - Purchases to import: " + purchaseList.size());
                            purchaseList.forEach(purchase -> {
                            log.info("****  IMPORT DONATE JOB  **** - Enviando dados de donate da preference: " + purchase.getReferenceInPaymentMethod() + " customer: " + cliente.getId() + " -  status pago: " + purchase.isPaid() + " -  imported: " + purchase.isImportedByServerCustomer() + " Method: " + purchase.getMethod().getName());
                            OkHttpClient httpClient = new OkHttpClient();
                            RequestBody requestBody = RequestBody.create(purchase.getWebShopReferenceCart(), MediaType.parse("application/json"));
                            String importUrl = cliente.getDomainIntegration() + "/api/v1/donate/user/" + purchase.getUsername() + "/purchase/" + purchase.getReferenceInPaymentMethod();
                            Request importDonateRequest = new Request.Builder()
                                    .url(importUrl)
                                    .post(requestBody)
                                    .build();
                            Response importedResponse = null;
                            try {
                                importedResponse = httpClient.newCall(importDonateRequest).execute();
                                if (!importedResponse.isSuccessful()) {
                                    log.error("****  IMPORT DONATE JOB -  ERROR  **** - erro ao importar donate, customer: " + cliente.getId() + " response: " + importedResponse.body().string());
                                }else{
                                    purchase.setImportedByServerCustomer(true);
                                    purchaseRepository.save(purchase);
                                    log.warn("****  IMPORT DONATE JOB -  SUCCESS **** - preference: " + purchase.getReferenceInPaymentMethod() + " - enviou para url:  POST " + importUrl + "Server Name: " + cliente.getServerFantasyName() + " - customerId: " + cliente.getId());

                                    try{
                                        DiscordRequestDTO discordNotification = DiscordRequestDTO.builder().content("****  COMPRA RECEBIDA PELO SERVIDOR **** - METHOD: " + purchase.getMethod().getName() + "  - referencia: " + purchase.getReferenceInPaymentMethod() + " - Usuario: " + purchase.getUsername() +  " - valor: " + purchase.getValue() + " - Server Name: " + cliente.getServerFantasyName() + " - customerId: " + cliente.getId()  + " - Carrinho (JSON): " + purchase.getWebShopReferenceCart()).build();
                                        Request discordNotificationRequest = new Request.Builder()
                                                .url(cliente.getDiscordWebhookNotifications())
                                                .post(RequestBody.create(MediaType.parse("application/json"), gson.toJson(discordNotification)))
                                                .build();
                                        httpClient.newCall(discordNotificationRequest).execute();
                                        log.info("****  IMPORT DONATE JOB (DISCORD) -  SUCCESS  **** - Notification enviada com sucesso, customer: " + cliente.getId());
                                    }catch (Exception e){
                                        log.info("****  IMPORT DONATE JOB (DISCORD) -  ERROR  **** - Notification não foi possível chegar ao destino, customer: " + cliente.getId());
                                    }
                                }
                            } catch (IOException e) {
                                log.error("****  IMPORT DONATE JOB -  ERROR  **** - Servidor não respondeu como deveria. Url: " + importDonateRequest.url() + " - Response: " + importedResponse);
                            }
                    });
        });

    }
}
