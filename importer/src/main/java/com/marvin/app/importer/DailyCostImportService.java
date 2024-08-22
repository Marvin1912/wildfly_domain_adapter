package com.marvin.app.importer;

import com.marvin.common.costs.DailyCostDTO;
import com.marvin.database.repository.DailyCostRepository;
import com.marvin.entities.costs.DailyCostEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DailyCostImportService implements ImportService<DailyCostDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DailyCostImportService.class);

    private final DailyCostRepository dailyCostRepository;

    public DailyCostImportService(DailyCostRepository dailyCostRepository) {
        this.dailyCostRepository = dailyCostRepository;
    }

    @Override
    public void importData(DailyCostDTO dailyCost) {
        dailyCostRepository.findByCostDateOrderByCostDate(dailyCost.costDate())
            .ifPresentOrElse(
                persistedState -> updateIfNecessary(persistedState, dailyCost),
                () -> saveNewDailyCost(dailyCost)
            );
    }

    private void updateIfNecessary(DailyCostEntity persistedState, DailyCostDTO dailyCost) {
        BigDecimal newValue = dailyCost.value();
        BigDecimal persistedValue = persistedState.getValue();
        if (newValue.compareTo(persistedValue) > 0) {
            LOGGER.info("Updated value of {} from {} to {}!", persistedState.getCostDate(), persistedValue, newValue);
            persistedState.setValue(newValue);
            dailyCostRepository.save(persistedState);
        }
    }

    private void saveNewDailyCost(DailyCostDTO dailyCost) {
        DailyCostEntity newDailyCostEntity = new DailyCostEntity(dailyCost.costDate(), dailyCost.value());
        dailyCostRepository.save(newDailyCostEntity);
    }
}
