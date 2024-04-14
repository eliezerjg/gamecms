package com.gamecms.backend.wyd.Services;

import com.gamecms.backend.wyd.DTO.DynamicTableResponseDTO;
import com.gamecms.backend.wyd.DTO.UpdatePurchaseRequestDTO;
import com.gamecms.backend.wyd.Exceptions.CustomerServerNaoEncontradoException;
import com.gamecms.backend.wyd.Exceptions.PurchaseNaoEncontradoException;
import com.gamecms.backend.wyd.Models.Purchase;
import com.gamecms.backend.wyd.Models.ServerCustomer;
import com.gamecms.backend.wyd.Repositories.PurchaseRepository;
import com.gamecms.backend.wyd.Repositories.ServerCustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseServiceImpl {
    private final PurchaseRepository repository;
    private final ServerCustomerRepository customerRepository;

    public DynamicTableResponseDTO getDynamicTableByCustomerId(Long customerId){
        List<Purchase> rows = repository.findAllByCustomerId(customerId);
        DynamicTableResponseDTO tableResultset = new DynamicTableResponseDTO();
        tableResultset.setRows(rows);
        tableResultset.setCols(List.of("id", "referenceinpaymentmethod", "dateinc", "username", "value", "paid", "importedbyservercustomer"));
        tableResultset.setEntriesModel(Purchase.getEntries());
        return tableResultset;
    }

    public Map<String, Object> findByExternalReferenceAndCustomerId(String externalReference, Long customerId){
        ServerCustomer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerServerNaoEncontradoException("findByExternalReferenceAndCustomerId - Server não encontrado: " + customerId));
        Purchase purchase = repository.findByCustomerIdAndReferenceInPaymentMethodEquals(customerId, externalReference).orElseThrow(() -> new PurchaseNaoEncontradoException("Compra não encontrada - Customer Id: "+customerId+"- reference: " + externalReference));

        Map<String, Object> purchaseDTO = Map.of(
                "externalReference", purchase.getReferenceInPaymentMethod(),
                "imported", purchase.isImportedByServerCustomer(),
                "value", purchase.getValue()
        );
        return  purchaseDTO;
    }


    public List<Map<String, Object>> findAllByUsername(String username, Long customerId){
        ServerCustomer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerServerNaoEncontradoException("findAllPurchasesByUsername - Server não encontrado: " + customerId));
        List<Purchase> preferenceList = repository.findAllByCustomerIdAndPaidIsTrueAndUsernameEquals(customerId, username);

        List<Map<String, Object>> purchasesDTO = new ArrayList<>();

        preferenceList.forEach(preference -> {
            Map<String, Object> purchaseDTO = Map.of(
                    "externalReference", preference.getReferenceInPaymentMethod(),
                    "imported", preference.isImportedByServerCustomer(),
                    "value", preference.getValue()
            );
            purchasesDTO.add(purchaseDTO);
        });

        return  purchasesDTO;
    }

    public void updateByIdAndCustomerId(Long customerId, Long preferenceId, UpdatePurchaseRequestDTO dto){
        log.info("Admin Atualizando Preference: " +preferenceId+ "Customer: " + customerId);
        Purchase purchase = repository.findByIdAndCustomerId(preferenceId, customerId).orElseThrow(() -> new PurchaseNaoEncontradoException("Preference não encontrada"));
        purchase.setPaid(dto.isPaid());
        purchase.setUsername(dto.getUsername());
        purchase.setValue(dto.getValue());
        repository.save(purchase);
        log.info("Admin Preference Atualizada: " +preferenceId+ " Customer: " + customerId);
    }

    public void deletebyIdAndCustomerId(Long customerId, Long preferenceId){
        log.info("Admin  deletando Preference: " +preferenceId+ " Customer: " + customerId);
        Purchase purchase = repository.findByIdAndCustomerId(preferenceId, customerId).orElseThrow(() -> new PurchaseNaoEncontradoException("Preference não encontrada"));
        repository.delete(purchase);
        log.info("Admin Preference deletada: " +preferenceId+ " Customer: " + customerId);
    }
}
