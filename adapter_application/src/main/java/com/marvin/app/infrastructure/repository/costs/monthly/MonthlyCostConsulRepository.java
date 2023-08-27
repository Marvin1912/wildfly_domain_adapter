package com.marvin.app.infrastructure.repository.costs.monthly;

import com.marvin.app.infrastructure.repository.costs.CostConsulRepository;
import com.marvin.consul.repository.BasicConsulRepository;
import org.springframework.stereotype.Component;

@Component
public class MonthlyCostConsulRepository extends CostConsulRepository {

    private static final String KEY_PREFIX = "costs/monthly";

    public MonthlyCostConsulRepository(BasicConsulRepository consulRepository) {
        super(consulRepository, KEY_PREFIX);
    }
}
