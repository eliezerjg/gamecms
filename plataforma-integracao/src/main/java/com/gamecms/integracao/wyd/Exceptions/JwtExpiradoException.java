package com.gamecms.integracao.wyd.Exceptions;

import com.gamecms.integracao.wyd.Exceptions.BaseException.ExceptionDefaultAbstractClass;
import com.gamecms.integracao.wyd.Exceptions.BaseException.IDefaultException;
import org.springframework.http.HttpStatus;

public class JwtExpiradoException extends ExceptionDefaultAbstractClass implements IDefaultException {

    public JwtExpiradoException(String customMessage) {
        this.setHttpStatus(HttpStatus.FORBIDDEN);
        this.setCustomMessage(customMessage);
    }

}
