package com.gamecms.backend.wyd.DTO;

import com.gamecms.backend.wyd.Models.ItemCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateShopRequestDTO {
    private Date dateAlt;
    private String title;
    private String description;
    private BigDecimal value;
    private String image;
    private ItemCategory category;
    private Long donateMultiplier;
}
