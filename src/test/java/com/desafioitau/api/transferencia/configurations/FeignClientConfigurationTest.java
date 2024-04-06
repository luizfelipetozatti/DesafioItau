package com.desafioitau.api.transferencia.configurations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class FeignClientConfigurationTest {

    @InjectMocks
    private FeignClientConfiguration feignClientConfiguration;

    @Test
    void feignRetryerBeanShouldBeConfigured() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(FeignClientConfiguration.class);
        context.refresh();

        assertTrue(context.containsBean("feignRetryer"));
        assertNotNull(context.getBean("feignRetryer"));
        assertEquals(CustomRetryer.class, context.getBean("feignRetryer").getClass());

        context.close();
    }
}
