package com.marvin.app.importer;

import com.marvin.common.costs.DailyCostDTO;
import com.marvin.database.repository.DailyCostRepository;
import com.marvin.entities.costs.DailyCostEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class DailyCostImportService implements ImportService<DailyCostDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DailyCostImportService.class);

    private final DailyCostRepository dailyCostRepository;

    public DailyCostImportService(DailyCostRepository dailyCostRepository) {
        this.dailyCostRepository = dailyCostRepository;
    }

    @Override
    public void importData(DailyCostDTO dailyCost) {
        final Optional<DailyCostEntity> persistedStateList = dailyCostRepository.findByCostDateOrderByCostDate(dailyCost.costDate());
        if (persistedStateList.isEmpty()) {
            DailyCostEntity monthlyCostEntity = new DailyCostEntity(dailyCost.costDate(), dailyCost.value());
            dailyCostRepository.save(monthlyCostEntity);
        } else {
            BigDecimal newValue = dailyCost.value();
            DailyCostEntity persistedState = persistedStateList.get();
            BigDecimal persistedValue = persistedState.getValue();
            if (newValue.compareTo(persistedValue) > 0) {
                LOGGER.info("Updated value of {} from {} to {}!", persistedState.getCostDate(), newValue, persistedValue);
                persistedState.setValue(newValue);
                dailyCostRepository.save(persistedState);
            }
        }
    }

}
