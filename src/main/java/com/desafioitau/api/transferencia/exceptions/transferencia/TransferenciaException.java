package com.desafioitau.api.transferencia.exceptions.transferencia;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class TransferenciaException extends Exception {
    protected TransferenciaException(Throwable cause) { super(cause); }
}
