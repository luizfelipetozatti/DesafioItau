package com.desafioitau.api.transferencia.v1.transferencia.service;

import com.desafioitau.api.transferencia.v1.transferencia.model.dto.TransferenciaDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransferenciaProducer {

    @Value("${transferencia.kafka.topicos.transferencias}")
    private String transferenciaTopico;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void enviarTransferencia(TransferenciaDTO transferenciaDTO) throws JsonProcessingException {
        log.info("Enviando para o kafka TransferenciaDTO {}.", transferenciaDTO);
        var json = objectMapper.writeValueAsString(transferenciaDTO);
        kafkaTemplate.send(transferenciaTopico, json);

    }

}
