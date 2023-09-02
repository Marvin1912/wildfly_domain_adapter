package com.marvin.app.configuration.influxdb;

import com.marvin.common.configuration.jms.JmsOperation;
import com.marvin.common.costs.daily.DailyCostDTO;
import com.marvin.influxdb.costs.daily.service.DailyCostImport;
import org.springframework.stereotype.Component;

@Component
public class DailyCostOperation implements JmsOperation<DailyCostDTO> {

    private final DailyCostImport dailyCostImport;

    public DailyCostOperation(DailyCostImport dailyCostImport) {
        this.dailyCostImport = dailyCostImport;
    }

    @Override
    public void executeOperation(DailyCostDTO dailyCost) {
        dailyCostImport.importCost(dailyCost);
    }
}
