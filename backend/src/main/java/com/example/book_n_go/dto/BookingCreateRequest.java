package com.example.book_n_go.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreateRequest {
    private Long hallId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
