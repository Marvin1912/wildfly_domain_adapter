package com.marvin.jms.configuration;

import jakarta.jms.Destination;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import javax.naming.Context;
import javax.naming.NamingException;

public class Destinations {

    /*
    Import
     */

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
    public Destination salaryQueueImport(
            Context namingContext,
            @Value("${jms.queue.costs.import.salary}") String queueName
    ) throws NamingException {
        return (Destination) namingContext.lookup(queueName);
    }

    /*
    Export
     */

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

    @Bean
    public Destination salaryQueueExport(
            Context namingContext,
            @Value("${jms.queue.costs.export.salary}") String queueName
    ) throws NamingException {
        return (Destination) namingContext.lookup(queueName);
    }

    @Bean
    public Destination dailyCostsQueueExport(
            Context namingContext,
            @Value("${jms.queue.costs.export.daily}") String queueName
    ) throws NamingException {
        return (Destination) namingContext.lookup(queueName);
    }

}
