package com.gamecms.backend.wyd.Exceptions;

import com.gamecms.backend.wyd.Exceptions.BaseException.ExceptionDefaultAbstractClass;
import com.gamecms.backend.wyd.Exceptions.BaseException.IDefaultException;
import org.springframework.http.HttpStatus;

public class ImagemNaoEncontradaException extends ExceptionDefaultAbstractClass implements IDefaultException {

    public ImagemNaoEncontradaException(String customMessage) {
        this.setHttpStatus(HttpStatus.NOT_FOUND);
        this.setCustomMessage(customMessage);
    }

}
