package com.desafioitau.api.transferencia.v1.cliente.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class ClienteException extends Exception {
    protected ClienteException(Throwable cause) { super(cause); }
}
