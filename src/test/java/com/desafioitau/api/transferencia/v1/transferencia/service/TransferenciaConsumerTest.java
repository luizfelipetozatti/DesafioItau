package com.desafioitau.api.transferencia.v1.transferencia.service;

import com.desafioitau.api.transferencia.v1.transferencia.facade.TransferenciaFacade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
class TransferenciaConsumerTest {

    private static final String TOPICO = "topico_teste";

    @MockBean
    private TransferenciaFacade transferenciaFacade;
    @Autowired
    private TransferenciaConsumer transferenciaConsumer;

    @Test
    void consumirTransferenciaDeveDarSucesso() throws Exception {
        Assertions.assertDoesNotThrow(() -> transferenciaConsumer.consumirTransferencia(any(), 0, TOPICO, 0));
    }

    @Test
    void consumirTransferenciaDeveDarException() throws Exception {
        doThrow(Exception.class).when(transferenciaFacade).consumirTransferencia(any());
        Assertions.assertThrows(Exception.class, () -> transferenciaConsumer.consumirTransferencia(any(), 0, TOPICO, 0));
    }

}
