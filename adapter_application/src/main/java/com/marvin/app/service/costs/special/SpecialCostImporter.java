package com.marvin.app.service.costs.special;

import com.marvin.camt.model.book_entry.BookingEntryDTO;
import com.marvin.camt.model.book_entry.CreditDebitCodeDTO;
import com.marvin.common.costs.special.SpecialCostDTO;
import com.marvin.common.costs.special.SpecialCostEntryDTO;
import com.marvin.jms.infrastructure.costs.special.SpecialCostDestination;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Set;

@Component
public class SpecialCostImporter {

    private final BigDecimal costLimit;
    private final SpecialCostDestination specialCostDestination;
    private final Set<String> specialCostBlockedIbans;

    public SpecialCostImporter(
            @Value("${camt.import.costs.special.limit:50}") int costLimit,
            SpecialCostDestination specialCostDestination,
            Set<String> specialCostBlockedIbans
    ) {
        this.costLimit = BigDecimal.valueOf(costLimit);
        this.specialCostDestination = specialCostDestination;
        this.specialCostBlockedIbans = specialCostBlockedIbans;
    }

    public Flux<String> importSpecialCost(Flux<BookingEntryDTO> bookEntryStream) {
        return bookEntryStream

                .filter(dto ->
                        dto.creditDebitCode() == CreditDebitCodeDTO.DBIT
                                && !specialCostBlockedIbans.contains(dto.creditIban())
                                && costLimit.compareTo(dto.amount()) <= 0
                )

                .groupBy(BookingEntryDTO::firstOfMonth)

                .flatMap(group -> group
                        .reduce(
                                new SpecialCostDTO(group.key(), new ArrayList<>()),
                                (specialCost, bookingEntry) -> {
                                    specialCost.entries()
                                            .add(new SpecialCostEntryDTO(
                                                    bookingEntry.entryInfo() + " - " + bookingEntry.creditName(),
                                                    bookingEntry.amount())
                                            );
                                    return specialCost;
                                }
                        )
                )
                .doOnNext(specialCostDestination::sendMessage)
                .map(specialCost -> "Processed " + specialCost + "!");
    }
}
