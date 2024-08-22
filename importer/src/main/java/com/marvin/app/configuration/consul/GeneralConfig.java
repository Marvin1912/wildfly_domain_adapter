package com.marvin.app.configuration.consul;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marvin.consul.repository.BasicConsulRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneralConfig {

    @Bean
    public BasicConsulRepository consulRepository(ObjectMapper objectMapper) {
        return new BasicConsulRepository(objectMapper);
    }

}
