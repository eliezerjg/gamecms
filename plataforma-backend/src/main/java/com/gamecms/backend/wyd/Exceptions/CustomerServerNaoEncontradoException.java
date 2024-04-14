package com.gamecms.backend.wyd.Exceptions;

import com.gamecms.backend.wyd.Exceptions.BaseException.ExceptionDefaultAbstractClass;
import com.gamecms.backend.wyd.Exceptions.BaseException.IDefaultException;
import org.springframework.http.HttpStatus;

public class CustomerServerNaoEncontradoException extends ExceptionDefaultAbstractClass implements IDefaultException {

    public CustomerServerNaoEncontradoException(String customMessage) {
        this.setHttpStatus(HttpStatus.NOT_FOUND);
        this.setCustomMessage(customMessage);
    }

}
