package com.desafioitau.api.transferencia.v1.transferencia.service;

import com.desafioitau.api.transferencia.v1.transferencia.facade.TransferenciaFacade;
import com.desafioitau.api.transferencia.v1.transferencia.model.dto.TransferenciaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransferenciaConsumer {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TransferenciaFacade transferenciaFacade;

    @KafkaListener(topics = "${transferencia.kafka.topicos.transferencias}",
            groupId = "${transferencia.kafka.group-id}")
    public void consumirTransferencia(String jsonPayload,
                                      @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                                      @Header(KafkaHeaders.RECEIVED_TOPIC) String topico,
                                      @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts) throws Exception {
        log.info("Consumindo TransferenciaDTO.");
        var transferenciaDTO = objectMapper.readValue(jsonPayload, TransferenciaDTO.class);
        transferenciaFacade.consumirTransferencia(transferenciaDTO);
    }
}
