package com.example.book_n_go.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Workspaces")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Workspace {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int providerId;
    @Column(nullable = false)
    private long locationId;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Location location;
    @Column(nullable = false)
    private double rating = 3;
    @Column(nullable = false)
    private String description;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User provider;
}
