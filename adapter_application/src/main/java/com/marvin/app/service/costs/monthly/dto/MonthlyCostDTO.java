package com.marvin.app.service.costs.monthly.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MonthlyCostDTO(LocalDate costDate, BigDecimal value) {
}
