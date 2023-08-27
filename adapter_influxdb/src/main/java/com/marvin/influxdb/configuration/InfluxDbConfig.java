package com.marvin.influxdb.configuration;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class InfluxDbConfig {

    @Bean
    public InfluxDBClient influxDBClient(
            @Value("${influxdb.token}") String token,
            @Value("${influxdb.url}") String url
    ) {
        return InfluxDBClientFactory.create(url, token.toCharArray());
    }

}
