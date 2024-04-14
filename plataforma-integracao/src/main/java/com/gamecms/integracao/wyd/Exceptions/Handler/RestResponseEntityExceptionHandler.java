package com.gamecms.integracao.wyd.Exceptions.Handler;

import com.gamecms.integracao.wyd.Exceptions.BaseException.ExceptionDefaultAbstractClass;
import com.gamecms.integracao.wyd.DTO.DefaultErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = { ExceptionDefaultAbstractClass.class})
    protected ResponseEntity<DefaultErrorDTO> handleConflict(ExceptionDefaultAbstractClass e) {
        DefaultErrorDTO error = DefaultErrorDTO
                                .builder()
                                .title("Error")
                                .message(e.getCustomMessage())
                                .build();

        log.error("Erro:", e);
        return new ResponseEntity<>(error,new HttpHeaders(), e.getHttpStatus());
    }
}
