package com.desafioitau.api.transferencia.v1.transferencia.service;

import com.desafioitau.api.transferencia.v1.transferencia.fixture.TransferenciaFixture;
import com.desafioitau.api.transferencia.v1.transferencia.mapper.TransferenciaMapper;
import com.desafioitau.api.transferencia.v1.transferencia.repository.TransferenciaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class TransferenciaServiceTest {

    @Mock
    private TransferenciaRepository repository;
    @InjectMocks
    private TransferenciaService service;

    @BeforeEach
    void reset() {
        Mockito.reset(repository);
    }

    @Test
    void salvarTransaferenciaDeveDarSucesso() {
        given(repository.save(any())).willReturn(TransferenciaFixture.getTransferencia());
        Assertions.assertDoesNotThrow(() -> service.salvarTransaferencia(TransferenciaFixture.getTransferenciaRequestDTO()));
    }

}
