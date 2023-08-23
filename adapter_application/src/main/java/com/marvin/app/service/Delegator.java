package com.marvin.app.service;

import com.marvin.camt.maintenance.DataMaintainer;
import com.marvin.camt.model.book_entry.BookingEntryDTO;
import com.marvin.app.model.event.NewFileEvent;
import com.marvin.camt.parser.CamtFileParser;
import com.marvin.camt.parser.DocumentUnmarshaller;
import com.marvin.app.service.costs.monthly.MonthlyCostImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.nio.file.Files;

@Component
public class Delegator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Delegator.class);

    private final CamtFileParser camtFileParser;
    private final DocumentUnmarshaller documentUnmarshaller;
    private final DataMaintainer maintainer;
    private final MonthlyCostImporter monthlyCostImporter;

    public Delegator(
            CamtFileParser camtFileParser,
            DocumentUnmarshaller documentUnmarshaller,
            DataMaintainer maintainer,
            MonthlyCostImporter monthlyCostImporter
    ) {
        this.camtFileParser = camtFileParser;
        this.documentUnmarshaller = documentUnmarshaller;
        this.maintainer = maintainer;
        this.monthlyCostImporter = monthlyCostImporter;
    }

    @EventListener(NewFileEvent.class)
    public void startUpWatchService(NewFileEvent newFileEvent) throws Exception {

        Flux<BookingEntryDTO> bookingEntryStream = getBookingEntries(Files.newInputStream(newFileEvent.path()))
                .publish().autoConnect(1);

        monthlyCostImporter.importMonthlyCost(bookingEntryStream)
                .subscribe(LOGGER::info);
    }

    private Flux<BookingEntryDTO> getBookingEntries(InputStream inputStream) {
        try {
            return documentUnmarshaller.unmarshallFile(camtFileParser.unzipFile(inputStream))
                    .flatMap(maintainer::maintainData);
        } catch (Exception e) {
            e.printStackTrace();
            return Flux.empty();
        }
    }
}
