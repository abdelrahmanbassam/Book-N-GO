package com.example.book_n_go.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HallRequest {
    long id;
    String name;
    int capacity;
    String description;
    double pricePerHour;
    double rating;
    Set<Long> aminitiesIds;
}
