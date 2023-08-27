package com.marvin.app.configuration.costs.special;

import com.marvin.app.infrastructure.repository.costs.special.SpecialCostConsulRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
public class SpecialCostConfig {

    private static final Logger LOGGER = Logger.getLogger(SpecialCostConfig.class.getName());

    private final SpecialCostConsulRepository specialCostConsulRepository;

    public SpecialCostConfig(SpecialCostConsulRepository specialCostConsulRepository) {
        this.specialCostConsulRepository = specialCostConsulRepository;
    }

    @Bean
    public Set<String> specialCostBlockedIbans() {

        String property = specialCostConsulRepository.getProperty("iban/blocked");

        Set<String> blockedIbans = Set.of(property.split(","));

        LOGGER.log(Level.INFO, "Initialized special cost blocked IBANs with: " + blockedIbans + "!");

        return blockedIbans;
    }
}
