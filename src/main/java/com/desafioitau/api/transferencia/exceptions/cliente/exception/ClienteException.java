package com.desafioitau.api.transferencia.exceptions.cliente.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class ClienteException extends Exception {
    protected ClienteException(Throwable cause) { super(cause); }
}
