package com.marvin.jms.infrastructure.costs.daily;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marvin.common.configuration.jms.JmsOperation;
import com.marvin.common.costs.daily.DailyCostDTO;
import jakarta.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;

import java.util.List;

public class DailyCostListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(DailyCostListener.class);

    private final ObjectMapper objectMapper;
    private final List<JmsOperation<DailyCostDTO>> operations;

    public DailyCostListener(
            ObjectMapper objectMapper,
            List<JmsOperation<DailyCostDTO>> operations
    ) {
        this.objectMapper = objectMapper;
        this.operations = operations;
    }

    @JmsListener(destination = "jms.queue.costs.export.daily", containerFactory = "listenerFactory")
    public void receiveMessage(TextMessage message) {
        try {
            DailyCostDTO dailyCost = objectMapper.readValue(message.getText(), DailyCostDTO.class);
            for (JmsOperation<DailyCostDTO> operation : operations) {
                operation.executeOperation(dailyCost);
            }
        } catch (Exception e) {
            LOGGER.error("Error reading MonthlyCost message!", e);
        }
    }
}
