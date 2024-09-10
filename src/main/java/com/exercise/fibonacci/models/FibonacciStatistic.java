package com.exercise.fibonacci.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FibonacciStatistic implements Serializable {

    @Serial
    private static final long serialVersionUID = 1245389229L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int number;
    private int requestCount;
    private LocalDate dateQuery;

    @PrePersist
    protected void onCreated(){
        this.dateQuery = ZonedDateTime.now(ZoneId.of("UTC")).toLocalDate();
    }
}
