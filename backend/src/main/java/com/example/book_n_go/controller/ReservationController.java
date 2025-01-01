package com.example.book_n_go.controller;

import com.example.book_n_go.dto.BookingUpdateStatusRequest;
import com.example.book_n_go.dto.ReservationRequest;
import com.example.book_n_go.enums.Status;
import com.example.book_n_go.model.User;
import com.example.book_n_go.service.AuthService;
import com.example.book_n_go.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final AuthService authService;

    public ReservationController(ReservationService reservationService, AuthService authService) {
        this.reservationService = reservationService;
        this.authService = authService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReservationRequest>> getAllReservations() {
        try {
            User user = authService.getRequestUser();
            System.out.println("user: " + user);
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            List<ReservationRequest> reservations = reservationService.getAllReservations(user);
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updateStatus")
    public ResponseEntity<String> updateStatus(@RequestBody BookingUpdateStatusRequest request) {
        if (request.getStatus() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            reservationService.UpdateBookingStatus(request);
            return new ResponseEntity<>("Status updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ReservationRequest>> getReservationsByStatus(@PathVariable("status") Status status) {
        try {
            User user = authService.getRequestUser();
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            List<ReservationRequest> reservations = reservationService.FilterReservationsByStatus(status, user);
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}