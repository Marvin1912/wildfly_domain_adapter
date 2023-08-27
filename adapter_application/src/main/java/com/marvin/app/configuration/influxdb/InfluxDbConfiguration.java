package com.marvin.app.configuration.influxdb;

import com.marvin.influxdb.configuration.InfluxDbConfig;
import com.marvin.influxdb.costs.monthly.service.MonthlyCostImport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(InfluxDbConfig.class)
@Configuration
public class InfluxDbConfiguration {
    @Bean
    public MonthlyCostImport monthlyCostImport() {
        return new MonthlyCostImport();
    }
}
