package com.marvin.app.importer;

import com.marvin.app.infrastructure.Ibans;
import com.marvin.camt.model.book_entry.BookingEntryDTO;
import com.marvin.camt.model.book_entry.CreditDebitCodeDTO;
import com.marvin.common.costs.SpecialCostDTO;
import com.marvin.common.costs.SpecialCostEntryDTO;
import com.marvin.database.repository.SpecialCostEntryRepository;
import com.marvin.entities.costs.SpecialCostEntity;
import com.marvin.entities.costs.SpecialCostEntryEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class SpecialCostImportService implements ImportService<SpecialCostDTO> {

    private final BigDecimal costLimit;
    private final Ibans specialCostBlockedIbans;
    private final SpecialCostEntryRepository specialCostEntryRepository;

    public SpecialCostImportService(
            @Value("${camt.import.costs.special.limit:50}") int costLimit,
            Ibans specialCostBlockedIbans,
            SpecialCostEntryRepository specialCostEntryRepository
    ) {
        this.costLimit = BigDecimal.valueOf(costLimit);
        this.specialCostBlockedIbans = specialCostBlockedIbans;
        this.specialCostEntryRepository = specialCostEntryRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Flux<String> importSpecialCost(Flux<BookingEntryDTO> bookEntryStream) {
        return bookEntryStream

                .filter(dto ->
                        dto.creditDebitCode() == CreditDebitCodeDTO.DBIT
                                && !specialCostBlockedIbans.getIbans().contains(dto.creditIban())
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
                .doOnNext(this::importData)
                .map(specialCost -> "Processed " + specialCost + "!");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void importData(SpecialCostDTO specialCost) {

        final List<SpecialCostEntryEntity> specialCostEntryEntities = specialCostEntryRepository.findBySpecialCostCostDate(specialCost.costDate());

        final List<SpecialCostEntryDTO> newEntries = specialCost.entries();

        // This means no special costs exist, so new ones need to be created
        if (specialCostEntryEntities.isEmpty()) {
            createAndPersistNewEntries(specialCost);
            return;
        }

        if (specialCostEntryEntities.size() < newEntries.size()) {
            createAndPersistNewEntries(specialCost, specialCostEntryEntities);
        }
    }

    private void createAndPersistNewEntries(SpecialCostDTO specialCost) {
        final SpecialCostEntity specialCostEntity = new SpecialCostEntity();
        specialCostEntity.setCostDate(specialCost.costDate());

        for (SpecialCostEntryDTO newEntry : specialCost.entries()) {
            createAndPersist(specialCostEntity, newEntry);
        }
    }

    private void createAndPersistNewEntries(SpecialCostDTO specialCost, List<SpecialCostEntryEntity> existingEntities) {
        for (SpecialCostEntryDTO newEntry : specialCost.entries()) {
            boolean isDuplicate = false;

            for (SpecialCostEntryEntity specialCostEntryEntity : existingEntities) {
                if (newEntry.description().equals(specialCostEntryEntity.getDescription())) {
                    isDuplicate = true;
                    break;
                }
            }

            if (!isDuplicate) {
                createAndPersist(existingEntities.get(0).getSpecialCost(), newEntry);
            }
        }
    }

    private void createAndPersist(SpecialCostEntity specialCostEntity, SpecialCostEntryDTO newEntry) {
        final SpecialCostEntryEntity specialCostEntryEntity = new SpecialCostEntryEntity();
        specialCostEntryEntity.setDescription(newEntry.description());
        specialCostEntryEntity.setValue(newEntry.value());
        specialCostEntryEntity.setSpecialCost(specialCostEntity);

        specialCostEntryRepository.save(specialCostEntryEntity);
    }

}
