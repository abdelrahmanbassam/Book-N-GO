package com.example.book_n_go.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Halls")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
//    @Column(nullable = false)
//    private int workspaceId;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Workspace workspace;
    @Column(nullable = false)
    private int capacity;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private double pricePerHour;
}
