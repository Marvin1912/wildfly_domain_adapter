package com.marvin.database.repository;

import com.marvin.entities.costs.DailyCostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyCostRepository extends JpaRepository<DailyCostEntity, Integer> {

    Optional<DailyCostEntity> findByCostDateOrderByCostDate(LocalDate costDate);

    List<DailyCostEntity> findByCostDateGreaterThanEqualOrderByCostDate(LocalDate localDate);

}
