package com.marvin.app.service.costs.salary;

import com.marvin.camt.model.book_entry.BookingEntryDTO;
import com.marvin.camt.model.book_entry.CreditDebitCodeDTO;
import com.marvin.common.costs.salary.SalaryDTO;
import com.marvin.jms.infrastructure.costs.salary.SalaryDestination;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Set;

@Component
public class SalaryImporter {

    private final SalaryDestination salaryDestination;
    private final Set<String> salaryIbans;

    public SalaryImporter(SalaryDestination salaryDestination, Set<String> salaryIbans) {
        this.salaryDestination = salaryDestination;
        this.salaryIbans = salaryIbans;
    }

    public Flux<String> importSalary(Flux<BookingEntryDTO> bookEntryStream) {
        return bookEntryStream

                .filter(dto ->
                        dto.creditDebitCode() == CreditDebitCodeDTO.CRDT
                                && salaryIbans.contains(dto.debitIban())
                )

                .map(dto -> new SalaryDTO(dto.firstOfMonth(), dto.amount()))

                .doOnNext(salaryDestination::sendMessage)
                .map(specialCost -> "Processed " + specialCost + "!");
    }
}
