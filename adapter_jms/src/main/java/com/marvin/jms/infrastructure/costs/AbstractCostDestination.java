package com.marvin.jms.infrastructure.costs;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;

public abstract class AbstractCostDestination implements CostDestination {

    protected ApplicationContext context;

    private final boolean jmsEnabled;

    public AbstractCostDestination(boolean jmsEnabled) {
        this.jmsEnabled = jmsEnabled;
    }

    protected abstract void sendCustomMessage(Object dto);

    @Override
    public void setApplicationContext(@NonNull ApplicationContext context) throws BeansException {
        this.context = context;
    }

    @Override
    public void sendMessage(Object dto) {
        if (!jmsEnabled) {
            return;
        }
        sendCustomMessage(dto);
    }
}
