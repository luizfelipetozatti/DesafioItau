package com.desafioitau.api.transferencia.v1.transferencia.controller;

import com.desafioitau.api.transferencia.v1.transferencia.facade.TransferenciaFacade;
import com.desafioitau.api.transferencia.v1.transferencia.fixture.TransferenciaFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TransferenciaController.class)
@AutoConfigureMockMvc
class TransferenciaControllerTest {

    private static final String V1_TRANSFERENCIAS = "/v1/transferencias";

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TransferenciaFacade facade;
    @Autowired
    private MockMvc mvc;

    @Test
    void enviarTransferenciaDeveDarSucesso() throws Exception {
        var jsonRequest = objectMapper.writeValueAsString(TransferenciaFixture.getTransferenciaRequestDTO());

        mvc.perform(post(V1_TRANSFERENCIAS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    /*static Stream<Triple<Class<? extends Exception>, ResultMatcher, ErrorInfo>> sourceEfetuarTransferencia() {
        return Stream.of(
                Triple.of(ClienteNotFoundException.class, status().isNotFound(), ErrorInfo.builder().mensagem(MessagesConstants.MSG_CLIENTE_NAO_ENCONTRADO).build()),
                Triple.of(ContaInternalServerErrorException.class, status().isInternalServerError(), ErrorInfo.builder().mensagem(MessagesConstants.MSG_ERRO_INTERNO_DO_SERVIDOR).build()),
                Triple.of(ContaServiceUnavailableException.class, status().isServiceUnavailable(), ErrorInfo.builder().mensagem(MessagesConstants.MSG_SERVICO_INDISPONIVEL).build()),
                Triple.of(ContaInternalErrorException.class, status().isBadRequest(), ErrorInfo.builder().build()),
                Triple.of(ClienteInternalServerErrorException.class, status().isInternalServerError(), ErrorInfo.builder().mensagem(MessagesConstants.MSG_ERRO_INTERNO_DO_SERVIDOR).build()),
                Triple.of(ClienteServiceUnavailableException.class, status().isServiceUnavailable(), ErrorInfo.builder().mensagem(MessagesConstants.MSG_SERVICO_INDISPONIVEL).build()),
                Triple.of(ClienteInternalErrorException.class, status().isBadRequest(), ErrorInfo.builder().build()),
                Triple.of(NotificacaoInternalServerErrorException.class, status().isInternalServerError(), ErrorInfo.builder().mensagem(MessagesConstants.MSG_ERRO_INTERNO_DO_SERVIDOR).build()),
                Triple.of(NotificacaoServiceUnavailableException.class, status().isServiceUnavailable(), ErrorInfo.builder().mensagem(MessagesConstants.MSG_SERVICO_INDISPONIVEL).build()),
                Triple.of(NotificacaoTentativasExcedidasException.class, status().isNotAcceptable(), ErrorInfo.builder().mensagem(MessagesConstants.MSG_TENTATIVAS_EXCEDIDAS).build()),
                Triple.of(NotificacaoInternalErrorException.class, status().isBadRequest(), ErrorInfo.builder().build()),
                Triple.of(ContaInativaException.class, status().isNotAcceptable(), ErrorInfo.builder().mensagem(MessagesConstants.MSG_CONTA_INATIVA).build()),
                Triple.of(ContaSaldoIndisponivelException.class, status().isNotAcceptable(), ErrorInfo.builder().mensagem(MessagesConstants.MSG_SALDO_INDISPONIVEL).build()),
                Triple.of(ContaLimiteDiarioInsuficienteException.class, status().isNotAcceptable(), ErrorInfo.builder().mensagem(MessagesConstants.MSG_LIMITE_DIARIO_INSUFICIENTE).build()),
                Triple.of(ContaLimiteDiarioZeradoException.class, status().isNotAcceptable(), ErrorInfo.builder().mensagem(MessagesConstants.MSG_LIMITE_DIARIO_ZERADO).build())
        );
    }

    @ParameterizedTest
    @MethodSource("sourceEfetuarTransferencia")
    void efetuarTransferenciaDeveDarExceptionQuandoEfetuarTransferencia(Triple<Class<? extends Exception>, ResultMatcher, ErrorInfo> source) throws Exception {
        given(facade.enviarTransferencia(any())).willThrow(source.getLeft());

        var jsonRequest = objectMapper.writeValueAsString(TransferenciaFixture.getTransferenciaRequestDTO());

        mvc.perform(post(V1_TRANSFERENCIAS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(source.getMiddle())
                .andExpect(content().json(objectMapper.writeValueAsString(source.getRight())));
    }*/
}
