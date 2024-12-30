package com.example.book_n_go.model;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.example.book_n_go.enums.Day;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Workdays")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Workday {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private LocalDateTime startTime;
    @Column(nullable = false)
    private LocalDateTime endTime;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Day weekDay;
    @ManyToOne
    // @JoinColumn(nullable = false)
    private Workspace workspace;
}
