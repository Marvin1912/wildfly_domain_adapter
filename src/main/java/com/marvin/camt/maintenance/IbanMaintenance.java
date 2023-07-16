package com.marvin.camt.maintenance;

import com.marvin.camt.model.book_entry.BookingEntryDTO;
import org.springframework.stereotype.Component;

@Component
public class IbanMaintenance implements BookEntryDataMaintenance {

    @Override
    public BookingEntryDTO applyToBookEntry(BookingEntryDTO bookingEntry) {
        return bookingEntry.creditIban() == null
                ? new BookingEntryDTO(
                bookingEntry.creditDebitCode(),
                bookingEntry.entryInfo(),
                bookingEntry.amount(),
                bookingEntry.bookingDate(),
                bookingEntry.firstOfMonth(),
                bookingEntry.debitName(),
                bookingEntry.debitIban(),
                bookingEntry.creditName(),
                "n/a"
        )
                : bookingEntry;
    }
}
