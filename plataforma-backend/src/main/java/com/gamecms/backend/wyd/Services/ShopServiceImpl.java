package com.gamecms.backend.wyd.Services;

import com.gamecms.backend.wyd.DTO.AdminSaveShopRequestDTO;
import com.gamecms.backend.wyd.DTO.AdminUpdateShopRequestDTO;
import com.gamecms.backend.wyd.DTO.DynamicTableResponseDTO;
import com.gamecms.backend.wyd.Exceptions.CustomerServerNaoEncontradoException;
import com.gamecms.backend.wyd.Exceptions.ShopItemNaoEncontradoException;
import com.gamecms.backend.wyd.Models.ServerCustomer;
import com.gamecms.backend.wyd.Models.ShopProduct;
import com.gamecms.backend.wyd.Repositories.ServerCustomerRepository;
import com.gamecms.backend.wyd.Repositories.ShopRepository;
import com.gamecms.backend.wyd.Exceptions.NoticiaNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopServiceImpl {
    private final ShopRepository repository;
    private final ServerCustomerRepository serverCustomerRepository;

    public Page<ShopProduct> findAllByCustomerId(Pageable pageable, Long customerId) {
        return repository.findAllByCustomerId(pageable, customerId);
    }

    public DynamicTableResponseDTO getDynamicShopTableByCustomerId(Long customerId){
        List<ShopProduct> rows = repository.findAllByCustomerId(customerId);
        DynamicTableResponseDTO tableResultset = new DynamicTableResponseDTO();
        tableResultset.setRows(rows);
        tableResultset.setCols(List.of("id", "image", "title", "description", "value"));
        tableResultset.setEntriesModel(ShopProduct.getEntries());
        return tableResultset;
    }


    public ShopProduct findItemShopByIdAndCustomerId(Long id, Long customerId) {
        return repository.findByIdAndCustomerId(id, customerId).orElseThrow(() -> new NoticiaNaoEncontradaException("Item não encontrado com ID: " + id));
    }

    public void deleteItemShopByIdAndCustomerId(Long customerId, Long shopId) {
        log.info("Admin - deletando item, item id: " + shopId + ", customer id: " + customerId);

        ShopProduct itemShopProduct = repository.findByIdAndCustomerId(shopId, customerId)
                .orElseThrow(() -> new ShopItemNaoEncontradoException("Item nao encontrado na base da dados."));
        repository.delete(itemShopProduct);

        log.info("Admin - deletou com sucesso o item, item id: " + shopId + ", customer id: " + customerId);
    }

    public void saveItemShopByCustomerId(Long customerId, AdminSaveShopRequestDTO dto) {
        log.info("Admin - Salvando novo item, title: " + dto.getTitle() + ", customer id: " + customerId);
        ServerCustomer customer = serverCustomerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerServerNaoEncontradoException(" Server nao encontrado na base"));

        ShopProduct itemShopProduct = new ShopProduct();
        itemShopProduct.setCustomer(customer);
        itemShopProduct.setDescription(dto.getDescription());
        itemShopProduct.setValue(dto.getValue());
        itemShopProduct.setTitle(dto.getTitle());
        itemShopProduct.setImage(dto.getImage());
        itemShopProduct.setCategory(dto.getCategory());

        if(dto.getDonateMultiplier() != null) {
            itemShopProduct.setDonateMultiplier(dto.getDonateMultiplier());
        }

        repository.save(itemShopProduct);
        log.info("Admin - Salvo com sucesso novo item, item: " + itemShopProduct.getId() + ", customer id: " + customerId);

    }

    public void updateItemShopByIdAndCustomerId(Long customerId, Long shopId, AdminUpdateShopRequestDTO dto) {
        log.info("Admin - Atualizando novo item, title: " + dto.getTitle() + ", customer id: " + customerId);

        ServerCustomer customer = serverCustomerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerServerNaoEncontradoException(" Server nao encontrado na base"));

        ShopProduct itemShopProduct = repository.findByIdAndCustomerId(shopId, customerId)
                .orElseThrow(() -> new ShopItemNaoEncontradoException("Item nao encontrado na base da dados."));

        itemShopProduct.setCustomer(customer);
        itemShopProduct.setDescription(dto.getDescription());
        itemShopProduct.setTitle(dto.getTitle());
        itemShopProduct.setValue(dto.getValue());
        itemShopProduct.setImage(dto.getImage());
        itemShopProduct.setCategory(dto.getCategory());

        if(dto.getDonateMultiplier() != null) {
            itemShopProduct.setDonateMultiplier(dto.getDonateMultiplier());
        }

        repository.save(itemShopProduct);

        log.info("Admin - Atualizado com sucesso novo item, item: " + itemShopProduct.getId() + ", customer id: " + customerId);
    }
}
