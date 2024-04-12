package com.desafioitau.api.transferencia.v1.transferencia.service;

import com.desafioitau.api.transferencia.v1.transferencia.fixture.TransferenciaFixture;
import com.desafioitau.api.transferencia.v1.transferencia.model.dto.TransferenciaDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest
class TransferenciaProducerTest {

    @MockBean
    private KafkaTemplate<String, TransferenciaDTO> kafkaTemplate;
    @Autowired
    private TransferenciaProducer transferenciaProducer;

    @Test
    void enviarTransferenciaDeveDarSucesso() {
        Assertions.assertDoesNotThrow(() -> transferenciaProducer.enviarTransferencia(TransferenciaFixture.getTransferenciaDTO()));
    }
}
