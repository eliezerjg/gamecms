package com.gamecms.backend.wyd.DTO;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DefaultErrorDTO {
    String title;
    String message;
}
