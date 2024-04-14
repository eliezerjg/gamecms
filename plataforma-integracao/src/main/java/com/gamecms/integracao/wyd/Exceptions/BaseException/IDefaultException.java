package com.gamecms.integracao.wyd.Exceptions.BaseException;

import org.springframework.http.HttpStatus;

public interface IDefaultException {
    public HttpStatus getHttpStatus();
    public String getCustomMessage();
}
