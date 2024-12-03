package com.example.book_n_go.model;

import java.sql.Time;

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
    private int workspaceId;
    @OneToMany
    @JoinColumn(name = "workspaceId", referencedColumnName = "id", insertable = false, updatable = false)
    private Workspace workspace;
    private Time startTime;
    private Time endTime;
    private Day day;
}
