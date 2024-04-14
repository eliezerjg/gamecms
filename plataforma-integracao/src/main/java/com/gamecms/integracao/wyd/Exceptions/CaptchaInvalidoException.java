package com.gamecms.integracao.wyd.Exceptions;

import com.gamecms.integracao.wyd.Exceptions.BaseException.ExceptionDefaultAbstractClass;
import com.gamecms.integracao.wyd.Exceptions.BaseException.IDefaultException;
import org.springframework.http.HttpStatus;

public class CaptchaInvalidoException extends ExceptionDefaultAbstractClass implements IDefaultException {

    public CaptchaInvalidoException(String customMessage) {
        this.setHttpStatus(HttpStatus.UNAUTHORIZED);
        this.setCustomMessage(customMessage);
    }

}
