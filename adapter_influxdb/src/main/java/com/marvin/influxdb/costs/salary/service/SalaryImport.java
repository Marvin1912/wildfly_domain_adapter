package com.marvin.influxdb.costs.salary.service;

import com.marvin.common.costs.salary.SalaryDTO;
import com.marvin.influxdb.costs.AbstractCostImport;
import com.marvin.influxdb.costs.salary.dto.SalaryMeasurement;

public class SalaryImport extends AbstractCostImport<SalaryDTO, SalaryMeasurement> {
    @Override
    protected SalaryMeasurement map(SalaryDTO salary) {
        return new SalaryMeasurement("salary", salary.value(), getAsInstant(salary.salaryDate()));
    }
}
