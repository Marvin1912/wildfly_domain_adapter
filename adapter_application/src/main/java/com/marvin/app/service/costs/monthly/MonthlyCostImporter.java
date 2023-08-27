package com.marvin.app.service.costs.monthly;

import com.marvin.camt.model.book_entry.BookingEntryDTO;
import com.marvin.camt.model.book_entry.CreditDebitCodeDTO;
import com.marvin.common.costs.monthly.MonthlyCostDTO;
import com.marvin.jms.infrastructure.costs.monthly.MonthlyCostDestination;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.Set;

@Component
public class MonthlyCostImporter {

    private final MonthlyCostDestination monthlyCostDestination;
    private final Set<String> monthlyCostBlockedIbans;

    public MonthlyCostImporter(MonthlyCostDestination monthlyCostDestination, Set<String> monthlyCostBlockedIbans) {
        this.monthlyCostDestination = monthlyCostDestination;
        this.monthlyCostBlockedIbans = monthlyCostBlockedIbans;
    }

    public Flux<String> importMonthlyCost(Flux<BookingEntryDTO> bookEntryStream) {
        return bookEntryStream

                .filter(dto ->
                        dto.creditDebitCode() == CreditDebitCodeDTO.DBIT
                                && !monthlyCostBlockedIbans.contains(dto.creditIban())
                )

                .groupBy(BookingEntryDTO::firstOfMonth)

                .flatMap(group -> group
                        .reduce(
                                new MonthlyCostDTO(group.key(), BigDecimal.ZERO),
                                (monthlyCostDTO, bookingEntryDTO) -> new MonthlyCostDTO(
                                        monthlyCostDTO.costDate(), monthlyCostDTO.value().add(bookingEntryDTO.amount())
                                )
                        )
                )
                .doOnNext(monthlyCostDestination::sendMessage)
                .map(monthlyCostDTO -> "Processed " + monthlyCostDTO + "!");
    }
}
