package com.marvin.common.costs.salary;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SalaryDTO(LocalDate costDate, BigDecimal value) {
}
