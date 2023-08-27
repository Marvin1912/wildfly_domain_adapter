package com.marvin.jms.infrastructure.costs.salary;

import com.marvin.jms.infrastructure.costs.AbstractCostDestination;
import jakarta.jms.Destination;
import org.springframework.jms.core.JmsTemplate;

public final class SalaryDestination extends AbstractCostDestination {

    private JmsTemplate jmsTemplate;
    private Destination salaryCostsQueueImport;

    public SalaryDestination(boolean jmsEnabled) {
        super(jmsEnabled);
    }

    @Override
    public void afterPropertiesSet() {
        this.salaryCostsQueueImport = context.getBean("salaryQueueImport", Destination.class);
        this.jmsTemplate = context.getBean("jmsTemplate", JmsTemplate.class);
    }

    @Override
    protected void sendCustomMessage(Object dto) {
        jmsTemplate.convertAndSend(salaryCostsQueueImport, dto);
    }
}
