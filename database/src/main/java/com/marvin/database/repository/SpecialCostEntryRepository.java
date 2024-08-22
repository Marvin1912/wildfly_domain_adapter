package com.marvin.database.repository;

import com.marvin.entities.costs.SpecialCostEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SpecialCostEntryRepository extends JpaRepository<SpecialCostEntryEntity, Integer> {

    List<SpecialCostEntryEntity> findBySpecialCostCostDate(LocalDate costDate);

    List<SpecialCostEntryEntity> findAllByOrderBySpecialCostCostDate();

}
