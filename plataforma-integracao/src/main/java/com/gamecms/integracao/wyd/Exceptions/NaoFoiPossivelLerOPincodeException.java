package com.gamecms.integracao.wyd.Exceptions;

import com.gamecms.integracao.wyd.Exceptions.BaseException.ExceptionDefaultAbstractClass;
import com.gamecms.integracao.wyd.Exceptions.BaseException.IDefaultException;
import org.springframework.http.HttpStatus;

public class NaoFoiPossivelLerOPincodeException extends ExceptionDefaultAbstractClass implements IDefaultException {

    public NaoFoiPossivelLerOPincodeException(String customMessage) {
        this.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        this.setCustomMessage(customMessage);
    }

}
