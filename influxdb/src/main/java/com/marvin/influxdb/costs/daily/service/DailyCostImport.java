package com.marvin.influxdb.costs.daily.service;

import com.marvin.common.costs.DailyCostDTO;
import com.marvin.influxdb.costs.AbstractCostImport;
import com.marvin.influxdb.costs.daily.dto.DailyCostMeasurement;

public class DailyCostImport extends AbstractCostImport<DailyCostDTO, DailyCostMeasurement> {
    @Override
    protected DailyCostMeasurement map(DailyCostDTO monthlyCost) {
        return new DailyCostMeasurement("daily", monthlyCost.value(), getAsInstant(monthlyCost.costDate()));
    }
}
