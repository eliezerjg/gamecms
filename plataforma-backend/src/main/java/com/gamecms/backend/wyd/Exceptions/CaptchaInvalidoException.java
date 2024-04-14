package com.gamecms.backend.wyd.Exceptions;

import com.gamecms.backend.wyd.Exceptions.BaseException.ExceptionDefaultAbstractClass;
import com.gamecms.backend.wyd.Exceptions.BaseException.IDefaultException;
import org.springframework.http.HttpStatus;

public class CaptchaInvalidoException extends ExceptionDefaultAbstractClass implements IDefaultException {

    public CaptchaInvalidoException(String customMessage) {
        this.setHttpStatus(HttpStatus.UNAUTHORIZED);
        this.setCustomMessage(customMessage);
    }

}
