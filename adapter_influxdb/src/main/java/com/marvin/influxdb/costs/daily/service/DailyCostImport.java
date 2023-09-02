package com.marvin.influxdb.costs.daily.service;

import com.marvin.common.costs.daily.DailyCostDTO;
import com.marvin.common.costs.monthly.MonthlyCostDTO;
import com.marvin.influxdb.costs.AbstractCostImport;
import com.marvin.influxdb.costs.daily.dto.DailyCostMeasurement;
import com.marvin.influxdb.costs.monthly.dto.MonthlyCostMeasurement;

public class DailyCostImport extends AbstractCostImport<DailyCostDTO, DailyCostMeasurement> {
    @Override
    protected DailyCostMeasurement map(DailyCostDTO monthlyCost) {
        return new DailyCostMeasurement("daily", monthlyCost.value(), getAsInstant(monthlyCost.costDate()));
    }
}
