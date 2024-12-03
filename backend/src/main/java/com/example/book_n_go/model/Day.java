package com.example.book_n_go.model;

import javax.persistence.Entity;
import javax.persistence.Table;

public enum Day {
    SATURDAY,
    SUNDAY,
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY
}

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
    private time startTime;
    private time endTime;
    private Day day;
}
