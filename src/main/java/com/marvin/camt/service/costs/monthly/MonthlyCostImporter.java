package com.marvin.camt.service.costs.monthly;

import com.marvin.camt.model.book_entry.BookingEntryDTO;
import com.marvin.camt.model.book_entry.CreditDebitCodeDTO;
import com.marvin.camt.service.costs.monthly.dto.MonthlyCostDTO;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.Set;

@Component
public class MonthlyCostImporter {

    private final Set<String> monthlyCostBlockedIbans;

    public MonthlyCostImporter(Set<String> monthlyCostBlockedIbans) {
        this.monthlyCostBlockedIbans = monthlyCostBlockedIbans;
    }

    public Flux<String> importMonthlyCost(Flux<BookingEntryDTO> bookEntryStream) {
        return bookEntryStream

                .filter(bookingEntryDTO -> bookingEntryDTO.creditDebitCode() == CreditDebitCodeDTO.DBIT
                        && !monthlyCostBlockedIbans.contains(bookingEntryDTO.creditIban()))

                .groupBy(BookingEntryDTO::firstOfMonth)

                .flatMap(group -> group
                        .reduce(
                                new MonthlyCostDTO(group.key(), BigDecimal.ZERO),
                                (monthlyCostDTO, bookingEntryDTO) -> new MonthlyCostDTO(
                                        monthlyCostDTO.costDate(), monthlyCostDTO.value().add(bookingEntryDTO.amount())
                                )
                        )
                )
                .map(monthlyCostDTO -> "Processed " + monthlyCostDTO + "!");
    }
}
