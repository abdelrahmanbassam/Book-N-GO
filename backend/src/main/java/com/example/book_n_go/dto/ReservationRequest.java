package com.example.book_n_go.dto;

import com.example.book_n_go.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequest {
     Long id;
     String HallName ;
     String clientName ;
     Status status ;
     Date date ;
     Time startTime;
     Time endTime ;
     String hallDescription ;

}
