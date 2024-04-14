package com.gamecms.integracao.wyd.DTO;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DefaultErrorDTO {
    String title;
    String message;
}
