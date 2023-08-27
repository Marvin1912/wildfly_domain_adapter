package com.marvin.app.configuration.costs.salary;

import com.marvin.app.infrastructure.repository.costs.salary.SalaryConsulRepository;
import com.marvin.app.infrastructure.repository.costs.special.SpecialCostConsulRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
public class SalaryConfig {

    private static final Logger LOGGER = Logger.getLogger(SalaryConfig.class.getName());

    private final SalaryConsulRepository salaryConsulRepository;

    public SalaryConfig(SalaryConsulRepository salaryConsulRepository) {
        this.salaryConsulRepository = salaryConsulRepository;
    }

    @Bean
    public Set<String> salaryIbans() {

        String property = salaryConsulRepository.getProperty("iban/import");

        Set<String> salaryIbans = Set.of(property.split(","));

        LOGGER.log(Level.INFO, "Initialized salary import IBANs with: " + salaryIbans + "!");

        return salaryIbans;
    }
}
