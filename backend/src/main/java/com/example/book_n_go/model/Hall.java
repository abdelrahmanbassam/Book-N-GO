package com.example.book_n_go.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
    private double rating;

    @ManyToOne
    // @JoinColumn(nullable = false)
    private Workspace workspace;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "hall_aminities",
            joinColumns = @JoinColumn(name = "hall_id"),
            inverseJoinColumns = @JoinColumn(name = "aminity_id")
    )
    @ToString.Exclude
    @JsonManagedReference
    private Set<Aminity> aminities;
}