package com.example.book_n_go.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Workspaces")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Workspace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    // @JoinColumn(nullable = false)
    private Location location;
    @ManyToOne
    // @JoinColumn(nullable = false)
    private User provider;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private double rating;
    @Column(nullable = false)
    private String description;
}
