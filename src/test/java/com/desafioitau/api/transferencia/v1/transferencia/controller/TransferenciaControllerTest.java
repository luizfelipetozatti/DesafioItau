package com.desafioitau.api.transferencia.v1.transferencia.controller;

import com.desafioitau.api.transferencia.v1.transferencia.facade.TransferenciaFacade;
import com.desafioitau.api.transferencia.v1.transferencia.fixture.TransferenciaFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(TransferenciaController.class)
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
        given(facade.enviarTransferencia(any())).willReturn(TransferenciaFixture.getTransferenciaDTO());
        var jsonRequest = objectMapper.writeValueAsString(TransferenciaFixture.getTransferenciaRequestDTO());

        mvc.perform(post(V1_TRANSFERENCIAS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

}
