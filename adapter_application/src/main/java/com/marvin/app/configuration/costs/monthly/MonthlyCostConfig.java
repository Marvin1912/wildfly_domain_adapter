package com.marvin.app.configuration.costs.monthly;

import com.marvin.app.infrastructure.repository.costs.monthly.MonthlyCostConsulRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
public class MonthlyCostConfig {

    private static final Logger LOGGER = Logger.getLogger(MonthlyCostConfig.class.getName());

    private final MonthlyCostConsulRepository monthlyCostConsulRepository;

    public MonthlyCostConfig(MonthlyCostConsulRepository monthlyCostConsulRepository) {
        this.monthlyCostConsulRepository = monthlyCostConsulRepository;
    }

    @Bean
    public Set<String> monthlyCostBlockedIbans() {

        String property = monthlyCostConsulRepository.getProperty("iban/blocked");

        LOGGER.log(Level.INFO, "Initialized monthly cost blocked IBANs with: "
                + (StringUtils.isBlank(property) ? "<empty>" : property) + "!");

        return Set.of(property.split(","));
    }
}
