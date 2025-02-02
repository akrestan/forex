package com.shipmonk.testingday.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shipmonk.testingday.entity.DayBase;

/**
 * @author Ales Krestan
 */
@Repository
public interface DayBaseRepository extends JpaRepository<DayBase, Long> {

    Optional<DayBase> findByDayAndBaseCode(String day, String baseCode);

}
