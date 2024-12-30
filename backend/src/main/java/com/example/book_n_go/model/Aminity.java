package com.example.book_n_go.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "Aminities")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Aminity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "aminities", fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<Hall> halls;
}