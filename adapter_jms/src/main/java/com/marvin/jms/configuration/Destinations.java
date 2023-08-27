package com.marvin.jms.configuration;

import jakarta.jms.Destination;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import javax.naming.Context;
import javax.naming.NamingException;

public class Destinations {

    @Bean
    public Destination monthlyCostsQueueImport(
            Context namingContext,
            @Value("${jms.queue.costs.import.monthly}") String queueName
    ) throws NamingException {
        return (Destination) namingContext.lookup(queueName);
    }

    @Bean
    public Destination specialCostsQueueImport(
            Context namingContext,
            @Value("${jms.queue.costs.import.special}") String queueName
    ) throws NamingException {
        return (Destination) namingContext.lookup(queueName);
    }

    @Bean
    public Destination monthlyCostsQueueExport(
            Context namingContext,
            @Value("${jms.queue.costs.export.monthly}") String queueName
    ) throws NamingException {
        return (Destination) namingContext.lookup(queueName);
    }

    @Bean
    public Destination specialCostsQueueExport(
            Context namingContext,
            @Value("${jms.queue.costs.export.special}") String queueName
    ) throws NamingException {
        return (Destination) namingContext.lookup(queueName);
    }

}
