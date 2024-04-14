package com.gamecms.backend.wyd.Exceptions.BaseException;

import lombok.Setter;
import org.springframework.http.HttpStatus;
@Setter
public abstract class ExceptionDefaultAbstractClass extends RuntimeException implements IDefaultException {
    private HttpStatus httpStatus;
    private String customMessage;

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
    @Override
    public String getCustomMessage() {
        return this.customMessage;
    }


}
