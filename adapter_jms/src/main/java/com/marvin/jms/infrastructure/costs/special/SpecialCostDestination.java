package com.marvin.jms.infrastructure.costs.special;

import com.marvin.jms.infrastructure.costs.AbstractCostDestination;
import jakarta.jms.Destination;
import org.springframework.jms.core.JmsTemplate;

public final class SpecialCostDestination extends AbstractCostDestination {

    private JmsTemplate jmsTemplate;
    private Destination specialCostsQueueImport;

    public SpecialCostDestination(boolean jmsEnabled) {
        super(jmsEnabled);
    }

    @Override
    public void afterPropertiesSet() {
        this.specialCostsQueueImport = context.getBean("specialCostsQueueImport", Destination.class);
        this.jmsTemplate = context.getBean("jmsTemplate", JmsTemplate.class);
    }

    @Override
    protected void sendCustomMessage(Object dto) {
        jmsTemplate.convertAndSend(specialCostsQueueImport, dto);
    }
}
