package com.example.book_n_go.model;


import com.example.book_n_go.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "Reservations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long workspaceId;

    @Column(nullable = false)
    private Long hallId;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private Time startTime;

    @Column(nullable = false)
    private Time endTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    //user has many reservations
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    //workspace has many reservations
    @ManyToOne
    @JoinColumn(name = "workspaceId", referencedColumnName = "id", insertable = false, updatable = false)
    private Workspace workspace;

    //hall has many reservations
    @ManyToOne
    @JoinColumn(name = "hallId", referencedColumnName = "id", insertable = false, updatable = false)
    private Hall hall;



}




