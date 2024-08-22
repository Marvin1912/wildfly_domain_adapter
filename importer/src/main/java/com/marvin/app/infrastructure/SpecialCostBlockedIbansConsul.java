package com.marvin.app.infrastructure;

import com.marvin.consul.repository.SpecialCostConsulRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("specialCostBlockedIbans")
public class SpecialCostBlockedIbansConsul implements Ibans {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecialCostBlockedIbansConsul.class);

    private final SpecialCostConsulRepository specialCostConsulRepository;

    public SpecialCostBlockedIbansConsul(SpecialCostConsulRepository specialCostConsulRepository) {
        this.specialCostConsulRepository = specialCostConsulRepository;
    }

    @Override
    public Set<String> getIbans() {
        String property = specialCostConsulRepository.getProperty("iban/blocked");

        Set<String> blockedIbans = Set.of(property.split(","));

        LOGGER.info("Initialized special cost blocked IBANs with: {}!", blockedIbans);

        return blockedIbans;
    }
}
