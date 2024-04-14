package com.gamecms.backend.wyd.DTO;

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
public class ShopDTO {

    private Long id;
    private Date dateInc;
    private Date dateAlt;
    private String title;
    private String description;
    private BigDecimal value;
    private int quantity;
    private String image;
}
