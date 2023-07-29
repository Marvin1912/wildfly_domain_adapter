package com.marvin.camt.service.costs.monthly;

import com.marvin.camt.model.book_entry.BookingEntryDTO;
import com.marvin.camt.model.book_entry.CreditDebitCodeDTO;
import com.marvin.camt.service.costs.monthly.dto.MonthlyCostDTO;
import jakarta.jms.Destination;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.Set;

@Component
public class MonthlyCostImporter {

    private final Set<String> monthlyCostBlockedIbans;
    private final Destination monthlyCostsQueue;
    private final JmsTemplate jmsTemplate;

    public MonthlyCostImporter(
            Set<String> monthlyCostBlockedIbans,
            Destination monthlyCostsQueue,
            JmsTemplate jmsTemplate
    ) {
        this.monthlyCostBlockedIbans = monthlyCostBlockedIbans;
        this.monthlyCostsQueue = monthlyCostsQueue;
        this.jmsTemplate = jmsTemplate;
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
                .doOnNext(monthlyCostDTO -> jmsTemplate.convertAndSend(monthlyCostsQueue, monthlyCostDTO))
                .map(monthlyCostDTO -> "Processed " + monthlyCostDTO + "!");
    }
}
