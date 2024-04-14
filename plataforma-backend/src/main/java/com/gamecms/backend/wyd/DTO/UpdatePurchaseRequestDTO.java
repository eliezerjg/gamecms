package com.gamecms.backend.wyd.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePurchaseRequestDTO {
    String username;
    BigDecimal value;
    boolean paid;
}
