package com.gamecms.backend.wyd.Exceptions;

import com.gamecms.backend.wyd.Exceptions.BaseException.ExceptionDefaultAbstractClass;
import com.gamecms.backend.wyd.Exceptions.BaseException.IDefaultException;
import org.springframework.http.HttpStatus;

public class NaoFoiPossivelCriarOLogOuImportException extends ExceptionDefaultAbstractClass implements IDefaultException {

    public NaoFoiPossivelCriarOLogOuImportException(String customMessage) {
        this.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        this.setCustomMessage(customMessage);
    }

}
