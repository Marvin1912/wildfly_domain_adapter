package com.marvin.app.infrastructure.repository.costs.special;

import com.marvin.app.infrastructure.repository.costs.CostConsulRepository;
import com.marvin.consul.repository.BasicConsulRepository;
import org.springframework.stereotype.Component;

@Component
public class SpecialCostConsulRepository extends CostConsulRepository {

    private static final String KEY_PREFIX = "costs/special";

    public SpecialCostConsulRepository(BasicConsulRepository consulRepository) {
        super(consulRepository, KEY_PREFIX);
    }
}
