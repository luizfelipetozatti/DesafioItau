package com.desafioitau.api.transferencia.v1.conta.exception.handler;

import com.desafioitau.api.transferencia.v1.constants.MessagesConstants;
import com.desafioitau.api.transferencia.v1.conta.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ContaExceptionHandler {


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {ContaInternalServerErrorException.class})
    @ResponseBody
    protected String handleContaInternalServerErrorException(ContaInternalServerErrorException ex) {
        return MessagesConstants.MSG_SERVICO_INDISPONIVEL;
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(value = {ContaInativaException.class,
            ContaSaldoIndisponivelException.class,
            ContaLimiteDiarioInsuficienteException.class,
            ContaLimiteDiarioZeradoException.class
    })
    @ResponseBody
    protected String handleNotAcceptableException(ContaException ex) {
        Map<Class<? extends ContaException>, String> exceptionMap = new HashMap<>();
        exceptionMap.put(ContaLimiteDiarioZeradoException.class, MessagesConstants.MSG_LIMITE_DIARIO_ZERADO);
        exceptionMap.put(ContaSaldoIndisponivelException.class, MessagesConstants.MSG_SALDO_INDISPONIVEL);
        exceptionMap.put(ContaLimiteDiarioInsuficienteException.class, MessagesConstants.MSG_LIMITE_DIARIO_INSUFICIENTE);

        String defaultMessage = MessagesConstants.MSG_CONTA_INATIVA;
        return exceptionMap.getOrDefault(ex.getClass(), defaultMessage);
    }
}
