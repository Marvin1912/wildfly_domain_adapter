package com.marvin.database.repository;

import com.marvin.entities.costs.SalaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SalaryRepository extends JpaRepository<SalaryEntity, Integer> {

    Optional<SalaryEntity> findBySalaryDate(LocalDate salaryDate);

    List<SalaryEntity> findAllByOrderBySalaryDate();

}
