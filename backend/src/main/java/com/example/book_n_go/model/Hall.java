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
    private int workspaceId;
    @ManyToOne
    @JoinColumn(name = "workspaceId", referencedColumnName = "id", insertable = false, updatable = false)
    private Workspace workspace;
    private int capacity;
    private String description;
    private double pricePerHour;
}
