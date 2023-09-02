package com.marvin.app.configuration.jms;

import com.marvin.jms.configuration.Destinations;
import com.marvin.jms.configuration.JmsConfig;
import com.marvin.jms.infrastructure.costs.daily.DailyCostListener;
import com.marvin.jms.infrastructure.costs.monthly.MonthlyCostDestination;
import com.marvin.jms.infrastructure.costs.monthly.MonthlyCostListener;
import com.marvin.jms.infrastructure.costs.salary.SalaryDestination;
import com.marvin.jms.infrastructure.costs.salary.SalaryListener;
import com.marvin.jms.infrastructure.costs.special.SpecialCostDestination;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
        JmsConfig.class,
        MonthlyCostListener.class,
        SalaryListener.class,
        DailyCostListener.class,
        Destinations.class
})
@Configuration
public class JmsConfiguration {

    @Bean
    public MonthlyCostDestination monthlyCostDestination(@Value("${jms.enabled}") boolean jmsEnabled) {
        return new MonthlyCostDestination(jmsEnabled);
    }

    @Bean
    public SpecialCostDestination specialCostDestination(@Value("${jms.enabled}") boolean jmsEnabled) {
        return new SpecialCostDestination(jmsEnabled);
    }

    @Bean
    public SalaryDestination salaryDestination(@Value("${jms.enabled}") boolean jmsEnabled) {
        return new SalaryDestination(jmsEnabled);
    }

}
