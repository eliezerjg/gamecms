package com.gamecms.integracao.wyd.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportCashDTO {
    @NotNull
    String user;

    @NotNull
    long donateValue;

}
