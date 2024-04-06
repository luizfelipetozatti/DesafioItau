package com.desafioitau.api.transferencia.v1.conta.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class ContaException extends Exception {
    protected ContaException(Throwable cause) { super(cause); }
}
