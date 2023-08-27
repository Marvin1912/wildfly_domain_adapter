package com.marvin.app.configuration.influxdb;

import com.marvin.common.configuration.jms.JmsOperation;
import com.marvin.common.costs.monthly.MonthlyCostDTO;
import com.marvin.influxdb.costs.monthly.service.MonthlyCostImport;
import org.springframework.stereotype.Component;

@Component
public class Operation implements JmsOperation<MonthlyCostDTO> {

    private final MonthlyCostImport monthlyCostImport;

    public Operation(MonthlyCostImport monthlyCostImport) {
        this.monthlyCostImport = monthlyCostImport;
    }

    @Override
    public void executeOperation(MonthlyCostDTO monthlyCost) {
        monthlyCostImport.importCost(monthlyCost);
    }
}
