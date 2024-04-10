package com.desafioitau.api.transferencia.v1.transferencia.controller;

import com.desafioitau.api.transferencia.v1.constants.MessagesConstants;
import com.desafioitau.api.transferencia.v1.transferencia.dto.TransferenciaDTO;
import com.desafioitau.api.transferencia.v1.transferencia.dto.TransferenciaRequestDTO;
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

@RestController
@RequestMapping("v1/transferencias")
public class TransferenciaController {

    @Autowired
    private TransferenciaFacade facade;

    @PostMapping
    @Operation(summary = "Endpoint responsável por realizar uma transferência entre contas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessagesConstants.MSG_TRANSFERENCIA_REALIZADA_COM_SUCESSO),
            @ApiResponse(responseCode = "404", description = MessagesConstants.MSG_CLIENTE_NAO_ENCONTRADO),
            @ApiResponse(responseCode = "406", description = MessagesConstants.MSG_CONTA_INATIVA + ", " +
                    MessagesConstants.MSG_LIMITE_DIARIO_ZERADO + ", " + MessagesConstants.MSG_SALDO_INDISPONIVEL + ", " +
                    MessagesConstants.MSG_LIMITE_DIARIO_INSUFICIENTE + ", " + MessagesConstants.MSG_TENTATIVAS_EXCEDIDAS),
            @ApiResponse(responseCode = "500", description = MessagesConstants.MSG_ERRO_INTERNO_DO_SERVIDOR),
            @ApiResponse(responseCode = "503", description = MessagesConstants.MSG_SERVICO_INDISPONIVEL),
    } )
    public ResponseEntity<TransferenciaDTO> efetuarTransferencia(@RequestBody TransferenciaRequestDTO transferenciaRequestDTO) throws Exception {
        return ResponseEntity.ok().body(facade.efetuarTransferencia(transferenciaRequestDTO));
    }
}
