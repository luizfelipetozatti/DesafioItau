package com.desafioitau.api.transferencia.exceptions.conta.exception.handler;

import com.desafioitau.api.transferencia.exceptions.ErrorInfo;
import com.desafioitau.api.transferencia.exceptions.conta.exception.*;
import com.desafioitau.api.transferencia.v1.constants.MessagesConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ContaExceptionHandler {

    private ErrorInfo traduzirException(String message) {
        return ErrorInfo.builder().mensagem(message).build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {ContaInternalServerErrorException.class})
    @ResponseBody
    protected ErrorInfo handleContaInternalServerErrorException(ContaInternalServerErrorException ex) {
        return traduzirException(MessagesConstants.MSG_ERRO_INTERNO_DO_SERVIDOR);
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(value = {ContaServiceUnavailableException.class})
    @ResponseBody
    protected ErrorInfo handleContaServiceUnavailableException(ContaServiceUnavailableException ex) {
        return traduzirException(MessagesConstants.MSG_SERVICO_INDISPONIVEL);
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(value = {ContaInativaException.class,
            ContaSaldoIndisponivelException.class,
            ContaLimiteDiarioInsuficienteException.class,
            ContaLimiteDiarioZeradoException.class
    })
    @ResponseBody
    protected ErrorInfo handleNotAcceptableException(ContaException ex) {
        Map<Class<? extends ContaException>, String> exceptionMap = new HashMap<>();
        exceptionMap.put(ContaLimiteDiarioZeradoException.class, MessagesConstants.MSG_LIMITE_DIARIO_ZERADO);
        exceptionMap.put(ContaSaldoIndisponivelException.class, MessagesConstants.MSG_SALDO_INDISPONIVEL);
        exceptionMap.put(ContaLimiteDiarioInsuficienteException.class, MessagesConstants.MSG_LIMITE_DIARIO_INSUFICIENTE);

        String defaultMessage = MessagesConstants.MSG_CONTA_INATIVA;
        return traduzirException(exceptionMap.getOrDefault(ex.getClass(), defaultMessage));
    }
}
