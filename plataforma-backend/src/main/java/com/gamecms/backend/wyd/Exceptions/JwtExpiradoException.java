package com.gamecms.backend.wyd.Exceptions;

import com.gamecms.backend.wyd.Exceptions.BaseException.ExceptionDefaultAbstractClass;
import com.gamecms.backend.wyd.Exceptions.BaseException.IDefaultException;
import org.springframework.http.HttpStatus;

public class JwtExpiradoException extends ExceptionDefaultAbstractClass implements IDefaultException {

    public JwtExpiradoException(String customMessage) {
        this.setHttpStatus(HttpStatus.FORBIDDEN);
        this.setCustomMessage(customMessage);
    }

}
