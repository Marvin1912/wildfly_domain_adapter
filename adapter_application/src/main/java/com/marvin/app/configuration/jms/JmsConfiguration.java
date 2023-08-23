package com.marvin.app.configuration.jms;

import com.marvin.jms.configuration.JmsConfig;
import com.marvin.jms.infrastructure.MonthlyCostDestination;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(JmsConfig.class)
@Configuration
public class JmsConfiguration {
    @Bean
    public MonthlyCostDestination monthlyCostDestination() {
        return new MonthlyCostDestination();
    }
}
