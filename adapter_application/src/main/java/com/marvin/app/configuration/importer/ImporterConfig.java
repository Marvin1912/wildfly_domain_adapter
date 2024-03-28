package com.marvin.app.configuration.importer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marvin.importer.costs.CostImporter;
import com.marvin.jms.infrastructure.costs.daily.DailyCostDestination;
import com.marvin.jms.infrastructure.costs.monthly.MonthlyCostDestination;
import com.marvin.jms.infrastructure.costs.salary.SalaryDestination;
import com.marvin.jms.infrastructure.costs.special.SpecialCostDestination;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImporterConfig {

    @Bean
    public CostImporter costImporter(
            @Value("${importer.in}") String in,
            @Value("${importer.done}") String done,
            ObjectMapper objectMapper,
            MonthlyCostDestination monthlyCostDestination,
            SalaryDestination salaryDestination,
            SpecialCostDestination specialCostDestination,
            DailyCostDestination dailyCostDestination
    ) {
        return new CostImporter(
                in, done,
                objectMapper,
                monthlyCostDestination,
                salaryDestination,
                specialCostDestination,
                dailyCostDestination
        );
    }
}
