package com.marvin.app.configuration.influxdb;

import com.marvin.common.configuration.jms.JmsOperation;
import com.marvin.common.costs.salary.SalaryDTO;
import com.marvin.influxdb.costs.salary.service.SalaryImport;
import org.springframework.stereotype.Component;

@Component
public class SalaryOperation implements JmsOperation<SalaryDTO> {

    private final SalaryImport salaryImport;

    public SalaryOperation(SalaryImport salaryImport) {
        this.salaryImport = salaryImport;
    }

    @Override
    public void executeOperation(SalaryDTO salary) {
        salaryImport.importCost(salary);
    }
}
