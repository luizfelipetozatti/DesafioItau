package com.desafioitau.api.transferencia.v1.cliente.exception.handler;

import com.desafioitau.api.transferencia.v1.cliente.exception.ClienteServiceUnavailableException;
import com.desafioitau.api.transferencia.v1.cliente.exception.ClienteInternalServerErrorException;
import com.desafioitau.api.transferencia.v1.cliente.exception.ClienteNotFoundException;
import com.desafioitau.api.transferencia.v1.constants.MessagesConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ClienteExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {ClienteNotFoundException.class})
    protected String handleClienteNotFoundException(ClienteNotFoundException ex) {
        return MessagesConstants.MSG_CLIENTE_NAO_ENCONTRADO;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {ClienteInternalServerErrorException.class})
    protected String handleClienteInternalServerErrorException(ClienteInternalServerErrorException ex) {
        return MessagesConstants.MSG_ERRO_INTERNO_DO_SERVIDOR;
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(value = {ClienteServiceUnavailableException.class})
    protected String handleClientServiceUnavailableException(ClienteServiceUnavailableException ex) {
        return MessagesConstants.MSG_SERVICO_INDISPONIVEL;
    }
}
