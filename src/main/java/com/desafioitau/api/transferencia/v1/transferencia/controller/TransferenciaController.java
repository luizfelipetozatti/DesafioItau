package com.desafioitau.api.transferencia.v1.transferencia.controller;

import com.desafioitau.api.transferencia.v1.constants.MessagesConstants;
import com.desafioitau.api.transferencia.v1.transferencia.facade.TransferenciaFacade;
import com.desafioitau.api.transferencia.v1.transferencia.model.dto.TransferenciaDTO;
import com.desafioitau.api.transferencia.v1.transferencia.model.dto.TransferenciaRequestDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/transferencias")
public class TransferenciaController {

    @Autowired
    private TransferenciaFacade facade;

    @PostMapping
    @Operation(summary = "Endpoint responsável por postar uma transferencia no kafka")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessagesConstants.MSG_TRANSFERENCIA_REALIZADA_COM_SUCESSO)
    } )
    public ResponseEntity<String> enviarTransferencia(@RequestBody TransferenciaRequestDTO transferenciaRequestDTO) throws JsonProcessingException {
        return ResponseEntity.ok().body(facade.enviarTransferencia(transferenciaRequestDTO).getIdTransferencia());
    }
}
