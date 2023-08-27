package com.marvin.app.service;

import com.marvin.app.model.event.NewFileEvent;
import com.marvin.app.service.costs.monthly.MonthlyCostImporter;
import com.marvin.app.service.costs.salary.SalaryImporter;
import com.marvin.app.service.costs.special.SpecialCostImporter;
import com.marvin.camt.maintenance.DataMaintainer;
import com.marvin.camt.model.book_entry.BookingEntryDTO;
import com.marvin.camt.parser.CamtFileParser;
import com.marvin.camt.parser.DocumentUnmarshaller;
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
    private final SpecialCostImporter specialCostImporter;
    private final SalaryImporter salaryImporter;

    public Delegator(
            CamtFileParser camtFileParser,
            DocumentUnmarshaller documentUnmarshaller,
            DataMaintainer maintainer,
            MonthlyCostImporter monthlyCostImporter,
            SpecialCostImporter specialCostImporter,
            SalaryImporter salaryImporter
    ) {
        this.camtFileParser = camtFileParser;
        this.documentUnmarshaller = documentUnmarshaller;
        this.maintainer = maintainer;
        this.monthlyCostImporter = monthlyCostImporter;
        this.specialCostImporter = specialCostImporter;
        this.salaryImporter = salaryImporter;
    }

    @EventListener(NewFileEvent.class)
    public void startUpWatchService(NewFileEvent newFileEvent) throws Exception {

        Flux<BookingEntryDTO> bookingEntryStream = getBookingEntries(Files.newInputStream(newFileEvent.path()))
                .publish().autoConnect(3);

        monthlyCostImporter.importMonthlyCost(bookingEntryStream)
                .subscribe(LOGGER::info);

        specialCostImporter.importSpecialCost(bookingEntryStream)
                .subscribe(LOGGER::info);

        salaryImporter.importSalary(bookingEntryStream)
                .subscribe(LOGGER::info);
    }

    private Flux<BookingEntryDTO> getBookingEntries(InputStream inputStream) {
        try {
            return documentUnmarshaller.unmarshallFile(camtFileParser.unzipFile(inputStream))
                    .flatMap(maintainer::maintainData);
        } catch (Exception e) {
            LOGGER.error("Error getting book entries!", e);
            return Flux.empty();
        }
    }
}
