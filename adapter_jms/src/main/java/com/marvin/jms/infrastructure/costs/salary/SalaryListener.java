package com.marvin.jms.infrastructure.costs.salary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marvin.common.configuration.jms.JmsOperation;
import com.marvin.common.costs.salary.SalaryDTO;
import jakarta.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;

import java.util.List;

public class SalaryListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalaryListener.class);

    private final ObjectMapper objectMapper;
    private final List<JmsOperation<SalaryDTO>> operations;

    public SalaryListener(
            ObjectMapper objectMapper,
            List<JmsOperation<SalaryDTO>> operations
    ) {
        this.objectMapper = objectMapper;
        this.operations = operations;
    }

    @JmsListener(destination = "jms.queue.costs.export.salary", containerFactory = "listenerFactory")
    public void receiveMessage(TextMessage message) {
        try {
            SalaryDTO monthlyCost = objectMapper.readValue(message.getText(), SalaryDTO.class);
            for (JmsOperation<SalaryDTO> operation : operations) {
                operation.executeOperation(monthlyCost);
            }
        } catch (Exception e) {
            LOGGER.error("Error reading Salary message!", e);
        }
    }
}
