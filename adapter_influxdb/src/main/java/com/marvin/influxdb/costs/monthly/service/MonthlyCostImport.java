package com.marvin.influxdb.costs.monthly.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.marvin.common.costs.monthly.MonthlyCostDTO;
import com.marvin.influxdb.costs.monthly.dto.MonthlyCostMeasurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class MonthlyCostImport implements ApplicationContextAware, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonthlyCostImport.class);

    private ApplicationContext context;
    private InfluxDBClient influxDBClient;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext context) throws BeansException {
        this.context = context;
    }

    @Override
    public void afterPropertiesSet() {
        this.influxDBClient = context.getBean("influxDBClient", InfluxDBClient.class);
    }

    public void importCost(MonthlyCostDTO monthlyCost) {

        LocalDate date = monthlyCost.costDate();
        Instant instant = date.atStartOfDay(ZoneId.of("UTC")).toInstant();

        MonthlyCostMeasurement monthly = new MonthlyCostMeasurement("monthly", monthlyCost.value(), instant);

        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
        writeApi.writeMeasurement("costs", "wildfly_domain", WritePrecision.NS, monthly);

        LOGGER.info("Imported InfluxDB measurement " + monthly);
    }
}
