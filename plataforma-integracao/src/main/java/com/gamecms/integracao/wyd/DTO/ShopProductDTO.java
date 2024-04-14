package com.gamecms.integracao.wyd.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShopProductDTO {
        private Long id;
        private String title;
        private String description;
        private BigDecimal value;
        private String image;
        private String category;
        private Long donateMultiplier;
}
