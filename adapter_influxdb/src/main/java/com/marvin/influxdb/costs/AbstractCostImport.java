package com.marvin.influxdb.costs;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
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

public abstract class AbstractCostImport<DTO, MEAS> implements ApplicationContextAware, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCostImport.class);

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

    protected abstract MEAS map(DTO dto);

    protected Instant getAsInstant(LocalDate localDate) {
        return localDate.atStartOfDay(ZoneId.of("UTC")).toInstant();
    }

    public void importCost(DTO dto) {
        MEAS measurement = map(dto);

        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
        writeApi.writeMeasurement("costs", "wildfly_domain", WritePrecision.NS, measurement);

        LOGGER.info("Imported InfluxDB measurement " + measurement);
    }
}
