package com.marvin.camt.configuration.flow;

import com.marvin.camt.model.book_entry.BookingEntryDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class FluxProcessors {

    @Bean
    public Sinks.Many<BookingEntryDTO> bookEntryProcessor() {
        return Sinks
                .many()
                .multicast()
                .onBackpressureBuffer(1024);
    }

}
