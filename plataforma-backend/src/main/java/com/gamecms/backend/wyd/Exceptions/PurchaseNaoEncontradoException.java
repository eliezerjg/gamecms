package com.gamecms.backend.wyd.Exceptions;

import com.gamecms.backend.wyd.Exceptions.BaseException.ExceptionDefaultAbstractClass;
import com.gamecms.backend.wyd.Exceptions.BaseException.IDefaultException;
import org.springframework.http.HttpStatus;

public class PurchaseNaoEncontradoException extends ExceptionDefaultAbstractClass implements IDefaultException {

    public PurchaseNaoEncontradoException(String customMessage) {
        this.setHttpStatus(HttpStatus.NOT_FOUND);
        this.setCustomMessage(customMessage);
    }

}
