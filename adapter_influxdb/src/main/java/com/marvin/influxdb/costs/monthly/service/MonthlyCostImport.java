package com.marvin.influxdb.costs.monthly.service;

import com.marvin.common.costs.monthly.MonthlyCostDTO;
import com.marvin.influxdb.costs.AbstractCostImport;
import com.marvin.influxdb.costs.monthly.dto.MonthlyCostMeasurement;

public class MonthlyCostImport extends AbstractCostImport<MonthlyCostDTO, MonthlyCostMeasurement> {
    @Override
    protected MonthlyCostMeasurement map(MonthlyCostDTO monthlyCost) {
        return new MonthlyCostMeasurement("monthly", monthlyCost.value(), getAsInstant(monthlyCost.costDate()));
    }
}
