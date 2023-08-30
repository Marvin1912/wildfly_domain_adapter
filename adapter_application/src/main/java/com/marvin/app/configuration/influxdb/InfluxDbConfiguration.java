package com.marvin.app.configuration.influxdb;

import com.marvin.influxdb.configuration.InfluxDbConfig;
import com.marvin.influxdb.costs.monthly.service.MonthlyCostImport;
import com.marvin.influxdb.costs.salary.service.SalaryImport;
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

    @Bean
    public SalaryImport salaryImport() {
        return new SalaryImport();
    }

}
