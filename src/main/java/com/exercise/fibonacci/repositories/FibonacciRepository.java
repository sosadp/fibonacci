package com.exercise.fibonacci.repositories;

import com.exercise.fibonacci.models.Fibonacci;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FibonacciRepository extends JpaRepository<Fibonacci, Long> {
    Optional<Fibonacci> findByNumber(int number);
}
