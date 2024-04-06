package com.desafioitau.api.transferencia.v1.transferencia.controller;

import com.desafioitau.api.transferencia.v1.constants.MessagesConstants;
import com.desafioitau.api.transferencia.v1.transferencia.dto.TransferenciaRequestDTO;
import com.desafioitau.api.transferencia.v1.transferencia.dto.TransferenciaResponseDTO;
import com.desafioitau.api.transferencia.v1.transferencia.facade.TransferenciaFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("v1/transferencias")
public class TransferenciaController {

    @Autowired
    private TransferenciaFacade facade;

    //TODO ajustar status code
    @PostMapping
    @Operation(summary = "Endpoint responsável por realizar uma transferência entre contas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessagesConstants.MSG_TRANSFERENCIA_REALIZADA_COM_SUCESSO),
            @ApiResponse(responseCode = "404", description = MessagesConstants.MSG_CLIENTE_NAO_ENCONTRADO),
//            @ApiResponse(responseCode = "404", description = MessagesConstants.MSG_CONTA_INATIVA),
//            @ApiResponse(responseCode = "")
    } )
    public ResponseEntity<TransferenciaResponseDTO> efetuarTransferencia(@RequestBody TransferenciaRequestDTO transferenciaRequestDTO) throws Exception {
        // implementar serviço de transferência
        facade.efetuarTransferencia(transferenciaRequestDTO);

        // retornar o idTransferencia
        var transferenciaResponseDTO = TransferenciaResponseDTO.builder()
                .idTransferencia(UUID.randomUUID())
                .build();
        return ResponseEntity.ok().body(transferenciaResponseDTO);
    }
}
