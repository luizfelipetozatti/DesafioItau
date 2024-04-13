package com.desafioitau.api.transferencia.configurations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class SpringDocConfigurationTest {

    private static final String HOST = "host";

    @InjectMocks
    private SpringDocConfiguration configuration;

    @Test
    void publicApiV1DeveDarSucesso() {

        var result = configuration.publicApiV1();

        assertEquals("v1", result.getGroup());
        assertEquals(Collections.singletonList("/v1/**"), result.getPathsToMatch());
    }

    @Test
    void customOpenAPIDeveDarSucesso() {

        var result = configuration.customOpenAPI(HOST);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getInfo());
    }
}
