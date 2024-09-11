package com.exercise.fibonacci.repositories;

import com.exercise.fibonacci.models.FibonacciStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FibonacciStatisticRepository extends JpaRepository<FibonacciStatistic, Long> {

    Optional<FibonacciStatistic> findByNumber(int n);

    Optional<List<FibonacciStatistic>> findByDateQueryGreaterThanEqual(LocalDate date);

}
