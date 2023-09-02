package com.marvin.common.costs.daily;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailyCostDTO(LocalDate costDate, BigDecimal value) {
}
