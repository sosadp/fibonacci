package com.exercise.fibonacci.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Table
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Fibonacci implements Serializable {

    @Serial
    private static final long serialVersionUID = 1245389494L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_sequence_generator")
    @SequenceGenerator(name = "my_sequence_generator", sequenceName = "sequence_fibonacci", allocationSize = 1)
    private Long id;
    @Column(unique = true)
    private int number;
    private long fibonacciValue;
}
