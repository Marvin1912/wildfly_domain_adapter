package com.marvin.common.costs.special;

import java.time.LocalDate;
import java.util.List;

public record SpecialCostDTO(LocalDate costDate, List<SpecialCostEntryDTO> entries) {
}
