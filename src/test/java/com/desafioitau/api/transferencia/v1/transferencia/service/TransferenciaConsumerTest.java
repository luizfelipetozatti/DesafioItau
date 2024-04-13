package com.desafioitau.api.transferencia.v1.transferencia.service;

import com.desafioitau.api.transferencia.exceptions.transferencia.TransferenciaClientException;
import com.desafioitau.api.transferencia.exceptions.transferencia.TransferenciaException;
import com.desafioitau.api.transferencia.v1.transferencia.facade.TransferenciaFacade;
import com.desafioitau.api.transferencia.v1.transferencia.fixture.TransferenciaFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class TransferenciaConsumerTest {

    private static final String TOPICO = "topico_teste";

    @Mock
    private TransferenciaFacade transferenciaFacade;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private TransferenciaConsumer transferenciaConsumer;

    @Test
    void consumirTransferenciaDeveDarSucesso() {
        Assertions.assertDoesNotThrow(() -> transferenciaConsumer.consumirTransferencia(TransferenciaFixture.getTransferenciaJson(), 0, TOPICO, 0));
    }

    @Test
    void consumirTransferenciaDeveDarException() throws Exception {
        doThrow(TransferenciaClientException.class).when(transferenciaFacade).consumirTransferencia(any());
        Assertions.assertThrows(TransferenciaException.class, () -> transferenciaConsumer.consumirTransferencia(TransferenciaFixture.getTransferenciaJson(), 0, TOPICO, 0));
    }

}
