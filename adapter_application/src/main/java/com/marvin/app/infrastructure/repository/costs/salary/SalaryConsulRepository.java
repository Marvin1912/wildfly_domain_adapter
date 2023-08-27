package com.marvin.app.infrastructure.repository.costs.salary;

import com.marvin.app.infrastructure.repository.costs.CostConsulRepository;
import com.marvin.consul.repository.BasicConsulRepository;
import org.springframework.stereotype.Component;

@Component
public class SalaryConsulRepository extends CostConsulRepository {

    private static final String KEY_PREFIX = "costs/salary";

    public SalaryConsulRepository(BasicConsulRepository consulRepository) {
        super(consulRepository, KEY_PREFIX);
    }
}
