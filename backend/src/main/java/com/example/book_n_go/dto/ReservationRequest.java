package com.example.book_n_go.dto;

import com.example.book_n_go.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequest {
     Long id;
     String HallName ;
     String clientName ;
     Status status ;
     private LocalDateTime startTime;
     private LocalDateTime endTime;
     String hallDescription ;

}
