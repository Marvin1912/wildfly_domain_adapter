package com.marvin.jms.infrastructure;

import jakarta.jms.Destination;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.lang.NonNull;

public class MonthlyCostDestination implements ApplicationContextAware, InitializingBean {

    private ApplicationContext context;

    private Destination monthlyCostsQueue;
    private JmsTemplate jmsTemplate;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext context) throws BeansException {
        this.context = context;
    }

    @Override
    public void afterPropertiesSet() {
        this.monthlyCostsQueue = context.getBean("monthlyCostsQueue", Destination.class);
        this.jmsTemplate = context.getBean("jmsTemplate", JmsTemplate.class);
    }

    public void sendMessage(Object dto) {
        jmsTemplate.convertAndSend(monthlyCostsQueue, dto);
    }
}
