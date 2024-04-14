package com.gamecms.backend.wyd.DTO.AbstractDTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractRecaptchaDTO {
    @NotNull
    public String recaptcha;
}
