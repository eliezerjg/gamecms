package com.gamecms.backend.wyd.Adapters;


import com.gamecms.backend.wyd.DTO.ShopDTO;
import com.mercadopago.client.preference.PreferenceItemRequest;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;



public class ShopInternalToMercadoPagoCartAdapter {
    private static final Currency currency = Currency.getInstance("BRL");

    private final List<ShopDTO> cartItems;

    public ShopInternalToMercadoPagoCartAdapter(List<ShopDTO> cartItems){
        this.cartItems = cartItems;
    }

    public List<PreferenceItemRequest> adapt() {
        return cartItems
                .stream()
                .map(itemShop -> PreferenceItemRequest.builder()
                        .id(String.valueOf(itemShop.getId()))
                        .title(itemShop.getTitle())
                        .description(itemShop.getDescription())
                        .categoryId(String.valueOf(itemShop.getId()))
                        .quantity(itemShop.getQuantity())
                        .unitPrice(itemShop.getValue())
                        .currencyId(currency.getCurrencyCode())
                        .build())
                .collect(Collectors.toList());
    }

}
