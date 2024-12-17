package com.example.book_n_go.model;

import com.example.book_n_go.enums.Aminity;

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
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int capacity;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private double pricePerHour;
    @Column(nullable = false)
    private double rating = 3;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Workspace workspace;

    @ElementCollection(targetClass = Aminity.class)
    @CollectionTable(name = "hall_aminities", joinColumns = @JoinColumn(name = "hall_id"))
    @Column(name = "aminity", nullable = false)
    @Enumerated(EnumType.STRING)
    private Aminity[] aminities;
}
