package com.marvin.jms.infrastructure.costs.monthly;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marvin.common.configuration.jms.JmsOperation;
import com.marvin.common.costs.monthly.MonthlyCostDTO;
import jakarta.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;

import java.util.List;

public class MonthlyCostListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonthlyCostListener.class);

    private final ObjectMapper objectMapper;
    private final List<JmsOperation<MonthlyCostDTO>> operations;

    public MonthlyCostListener(
            ObjectMapper objectMapper,
            List<JmsOperation<MonthlyCostDTO>> operations
    ) {
        this.objectMapper = objectMapper;
        this.operations = operations;
    }

    @JmsListener(destination = "jms.queue.costs.export.monthly", containerFactory = "listenerFactory")
    public void receiveMessage(TextMessage message) {
        try {
            MonthlyCostDTO monthlyCost = objectMapper.readValue(message.getText(), MonthlyCostDTO.class);
            for (JmsOperation<MonthlyCostDTO> operation : operations) {
                operation.executeOperation(monthlyCost);
            }
        } catch (Exception e) {
            LOGGER.error("Error reading MonthlyCost message!", e);
        }
    }
}
