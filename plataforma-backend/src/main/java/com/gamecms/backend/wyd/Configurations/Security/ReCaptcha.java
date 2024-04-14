package com.gamecms.backend.wyd.Configurations.Security;

import com.gamecms.backend.wyd.Exceptions.CaptchaInvalidoException;
import com.gamecms.backend.wyd.DTO.AbstractDTO.AbstractBasicAuthDTO;
import com.gamecms.backend.wyd.DTO.AbstractDTO.AbstractRecaptchaDTO;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;

public class ReCaptcha {
    public static final String SECRET_KEY = "6LcciB0pAAAAAIH90DbaCxTFvWSZgdeBJhV9QvmE";

    public static void validateCaptchaOrThrow(AbstractRecaptchaDTO dto) throws CaptchaInvalidoException {
        doValidate(dto.getRecaptcha());
    }

    public static void validateCaptchaOrThrow(AbstractBasicAuthDTO dto) throws CaptchaInvalidoException {
        doValidate(dto.getRecaptcha());
    }

    public static void validateCaptchaOrThrow(String recaptcha) throws CaptchaInvalidoException {
        doValidate(recaptcha);
    }

    private static void doValidate(String captcha){
        if(captcha == null || captcha.isEmpty() || captcha.equals("undefined")){
            throw new CaptchaInvalidoException("Erro Captcha inválido.");
        }
        String url = "https://www.google.com/recaptcha/api/siteverify?secret=" + SECRET_KEY + "&response=" + captcha;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try{
            Response response = client.newCall(request).execute();
            String responseBody = Objects.requireNonNull(response.body()).string();

            if(!responseBody.contains("success")){
                throw new CaptchaInvalidoException("Erro Captcha inválido.");
            }
        }catch (IOException e){
            throw new CaptchaInvalidoException("Erro Captcha inválido.");
        }
    }

}
