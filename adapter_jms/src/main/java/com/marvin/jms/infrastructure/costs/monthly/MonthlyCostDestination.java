package com.marvin.jms.infrastructure.costs.monthly;

import com.marvin.jms.infrastructure.costs.AbstractCostDestination;
import jakarta.jms.Destination;
import org.springframework.jms.core.JmsTemplate;

public final class MonthlyCostDestination extends AbstractCostDestination {

    private JmsTemplate jmsTemplate;
    private Destination monthlyCostsQueueImport;

    public MonthlyCostDestination(boolean jmsEnabled) {
        super(jmsEnabled);
    }

    @Override
    public void afterPropertiesSet() {
        this.monthlyCostsQueueImport = context.getBean("monthlyCostsQueueImport", Destination.class);
        this.jmsTemplate = context.getBean("jmsTemplate", JmsTemplate.class);
    }

    @Override
    protected void sendCustomMessage(Object dto) {
        jmsTemplate.convertAndSend(monthlyCostsQueueImport, dto);
    }
}
