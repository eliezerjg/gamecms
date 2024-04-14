package com.gamecms.backend.wyd.DTO;

import lombok.Data;

import java.util.List;

@Data
public class CreatePreferenceRequestDTO {
    List<ShopDTO> cart;
    String username;
}
