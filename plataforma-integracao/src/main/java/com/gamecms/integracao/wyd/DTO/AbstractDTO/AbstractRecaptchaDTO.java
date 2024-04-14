package com.gamecms.integracao.wyd.DTO.AbstractDTO;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public abstract class AbstractRecaptchaDTO {
    @NotNull
    public String recaptcha;
}
