package com.gamecms.backend.wyd.DTO;

import com.gamecms.backend.wyd.Models.ItemCategory;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;


@Data
@Builder
public class SendDonateToIntegrationShopProductDTO {
    private Long id;
    private String title;
    private String description;
    private BigDecimal value;
    private ItemCategory category;
    private Long donateMultiplier;
}
