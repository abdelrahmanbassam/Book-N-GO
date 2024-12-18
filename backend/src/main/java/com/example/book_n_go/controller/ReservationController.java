package com.example.book_n_go.controller;

import com.example.book_n_go.dto.BookingUpdateStatusRequest;
import com.example.book_n_go.dto.ReservationRequest;
import com.example.book_n_go.enums.Status;
import com.example.book_n_go.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationsService;

    @GetMapping("/all")
    public ResponseEntity<List<ReservationRequest>> getAllReservations() {
        List<ReservationRequest> reservations = reservationsService.getAllReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }
    // update status
    @PutMapping("/status/update")
    public ResponseEntity<String> updateStatus(@RequestBody BookingUpdateStatusRequest bookingUpdateStatusRequest) {
        reservationsService.UpdateBookingStatus(bookingUpdateStatusRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    // filter by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ReservationRequest>> getReservationsByStatus(@PathVariable Status status) {
        List<ReservationRequest> reservations = reservationsService.FilterReservationsByStatus(status);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }


//    @GetMapping("/client/{id}")
//    public ResponseEntity<List<ReservationRequest>> getClientReservations(@PathVariable Long id) {
//        List<ReservationRequest> reservations = reservationsService.getClientReservations(id);
//          return  new ResponseEntity<>(reservations, HttpStatus.OK);
//    }
//    @GetMapping("/client/{id}/{status}")
//    public ResponseEntity<ArrayList<ReservationRequest>> getClientReservationsByStatus(@PathVariable Long id, @PathVariable Status status) {
//        ArrayList<ReservationRequest> reservations = reservationsService.getClientReservationsByStatus(id, status);
//          return  new ResponseEntity<>(reservations, HttpStatus.OK);
//    }
//    @GetMapping("/provider/{id}")
//    public ResponseEntity<ArrayList<ReservationRequest>> getProviderReservations(@PathVariable Long id) {
//        ArrayList<ReservationRequest> reservations = reservationsService.getProviderReservations(id);
//          return  new ResponseEntity<>(reservations, HttpStatus.OK);
//    }
//    @GetMapping("/provider/{id}/{status}")
//    public ResponseEntity<ArrayList<ReservationRequest>> getProviderReservationsByStatus(@PathVariable Long id, @PathVariable Status status) {
//        ArrayList<ReservationRequest> reservations = reservationsService.getProviderReservationsByStatus(id, status);
//        return  new ResponseEntity<>(reservations, HttpStatus.OK);
//    }

//    @PostMapping("/accept")
//    public ResponseEntity<HttpStatus> acceptReservation(@RequestBody Long reservationId) {
//        System.out.println("reservationId = " + reservationId);
////             reservationsService.acceptReservation(reservationId);
//            return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @PostMapping("/reject")
//    public ResponseEntity<String> rejectReservation(@RequestBody Long reservationId) {
//        System.out.println("reservationId = " + reservationId);
//        reservationsService.rejectReservation(reservationId);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @PostMapping("/cancel")
//    public ResponseEntity<String> cancelReservation(@RequestBody Long reservationId) {
//        System.out.println("reservationId = " + reservationId);
//        reservationsService.cancelReservation(reservationId);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }



}