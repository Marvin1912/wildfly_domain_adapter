package com.marvin.database.repository;

import com.marvin.entities.costs.MonthlyCostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MonthlyCostRepository extends JpaRepository<MonthlyCostEntity, Integer> {

    Optional<MonthlyCostEntity> findByCostDate(LocalDate costDate);

    List<MonthlyCostEntity> findAllByOrderByCostDate();

}
