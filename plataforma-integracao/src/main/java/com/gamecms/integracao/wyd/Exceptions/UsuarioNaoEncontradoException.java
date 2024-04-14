package com.gamecms.integracao.wyd.Exceptions;

import com.gamecms.integracao.wyd.Exceptions.BaseException.ExceptionDefaultAbstractClass;
import com.gamecms.integracao.wyd.Exceptions.BaseException.IDefaultException;
import org.springframework.http.HttpStatus;

public class UsuarioNaoEncontradoException extends ExceptionDefaultAbstractClass implements IDefaultException {

    public UsuarioNaoEncontradoException(String customMessage) {
        this.setHttpStatus(HttpStatus.NOT_FOUND);
        this.setCustomMessage(customMessage);
    }

}
