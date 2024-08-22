package com.marvin.app.infrastructure;

import com.marvin.consul.repository.MonthlyCostConsulRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("monthlyCostBlockedIbans")
public class MonthlyCostBlockedIbansConsul implements Ibans {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonthlyCostBlockedIbansConsul.class);

    private final MonthlyCostConsulRepository monthlyCostConsulRepository;

    public MonthlyCostBlockedIbansConsul(MonthlyCostConsulRepository monthlyCostConsulRepository) {
        this.monthlyCostConsulRepository = monthlyCostConsulRepository;
    }

    @Override
    public Set<String> getIbans() {

        String property = monthlyCostConsulRepository.getProperty("iban/blocked");

        Set<String> blockedIbans = Set.of(property.split(","));

        LOGGER.info("Initialized monthly cost blocked IBANs with: {}!", blockedIbans);

        return blockedIbans;

    }
}
