package com.marvin.jms.infrastructure.costs.daily;

import com.marvin.jms.infrastructure.costs.AbstractCostDestination;
import jakarta.jms.Destination;
import org.springframework.jms.core.JmsTemplate;

public final class DailyCostDestination extends AbstractCostDestination {

    private JmsTemplate jmsTemplate;
    private Destination dailyCostQueueImport;

    public DailyCostDestination(boolean jmsEnabled) {
        super(jmsEnabled);
    }

    @Override
    public void afterPropertiesSet() {
        this.dailyCostQueueImport = context.getBean("dailyCostQueueImport", Destination.class);
        this.jmsTemplate = context.getBean("jmsTemplate", JmsTemplate.class);
    }

    @Override
    protected void sendCustomMessage(Object dto) {
        jmsTemplate.convertAndSend(dailyCostQueueImport, dto);
    }
}
