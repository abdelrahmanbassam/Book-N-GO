package com.example.book_n_go.model;

import lombok.*;
import javax.persistence.*;

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
    private int providerId;
    private int locationId;
    @ManyToOne
    @JoinColumn(name = "locationId", referencedColumnName = "id", insertable = false, updatable = false)
    private Location location;
}
