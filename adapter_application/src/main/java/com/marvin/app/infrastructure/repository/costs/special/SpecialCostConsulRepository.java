package com.marvin.app.infrastructure.repository.costs.special;

import com.marvin.consul.model.ConsulKeyValueDTO;
import com.marvin.consul.repository.BasicConsulRepository;
import com.marvin.consul.repository.ConsulRepository;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class SpecialCostConsulRepository implements ConsulRepository {

    private static final Logger LOGGER = Logger.getLogger(SpecialCostConsulRepository.class.getName());

    private static final String KEY_PREFIX = "costs/special";

    private Map<String, String> properties;
    private final BasicConsulRepository consulRepository;

    public SpecialCostConsulRepository(BasicConsulRepository consulRepository) {
        this.properties = new HashMap<>();
        this.consulRepository = consulRepository;
    }

    @PostConstruct
    public void init() {
        try {
            this.properties = consulRepository.getPropertiesRecursively(KEY_PREFIX)
                    .stream()
                    .filter(dto -> dto != null && dto.key() != null && dto.value() != null)
                    .collect(Collectors.toMap(ConsulKeyValueDTO::key, ConsulKeyValueDTO::value));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during initialization!!!", e);
            this.properties = Collections.emptyMap();
        }
    }

    @Override
    public String getProperty(String key) {
        final String value = properties.get(KEY_PREFIX + "/" + key);
        return StringUtils.isBlank(value) ? StringUtils.EMPTY : value;
    }
}
