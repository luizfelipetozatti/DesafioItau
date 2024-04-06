package com.desafioitau.api.transferencia.configurations;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfiguration {

    @Bean
    public Retryer feignRetryer() {
        return new CustomRetryer();
    }
}
