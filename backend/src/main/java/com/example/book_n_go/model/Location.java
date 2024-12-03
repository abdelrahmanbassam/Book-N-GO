package com.example.book_n_go.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Locations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int departmentNumber;
    private int streetNumber;
    private String city;
}
