package com.example.book_n_go.service;

import com.example.book_n_go.dto.BookingUpdateStatusRequest;
import com.example.book_n_go.dto.ReservationRequest;
import com.example.book_n_go.enums.Role;
import com.example.book_n_go.enums.Status;
import com.example.book_n_go.model.Booking;
import com.example.book_n_go.model.User;
import com.example.book_n_go.repository.BookingRepo;
import com.example.book_n_go.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private UserRepo userRepo;
    public List<ReservationRequest> getAllReservations(User currentUser) {
        ArrayList<ReservationRequest> reservationsRequest = new ArrayList<>(List.of());
        List<Booking> bookings;
        if (currentUser.getRole() == Role.CLIENT) {
            bookings = bookingRepo.findByUserId(currentUser.getId());
        } else {
            bookings = bookingRepo.findByProviderId(currentUser.getId());
        }
        for (Booking book : bookings) {
            ReservationRequest reservationRequest = getReservationRequest(book);
            reservationsRequest.add(reservationRequest);
        }
        return reservationsRequest;
    }

    private ReservationRequest getReservationRequest(Booking Book) {
        ReservationRequest reservationRequest = new ReservationRequest();
        reservationRequest.setId(Book.getId());
        reservationRequest.setHallName(Book.getHall().getName());
        reservationRequest.setClientName(Book.getUser().getName());
        reservationRequest.setStatus(Book.getStatus());
        reservationRequest.setStartTime(Book.getStartTime());
        reservationRequest.setEndTime(Book.getEndTime());
        reservationRequest.setHallDescription(Book.getHall().getDescription());
        return reservationRequest;
    }

    public void UpdateBookingStatus(BookingUpdateStatusRequest bookingUpdateStatusRequest) {
        long bookingId = bookingUpdateStatusRequest.getBookingId();
        Status status = bookingUpdateStatusRequest.getStatus();
        Optional<Booking> optionalBooking = bookingRepo.findById(bookingId);
        if (optionalBooking.isPresent()) {
            if (status != null) {
                Booking booking = optionalBooking.get();
                booking.setStatus(status);
                bookingRepo.save(booking);
            } else {
                throw new IllegalArgumentException("Invalid status: null");
            }
        } else {
            throw new IllegalArgumentException("Booking not found with id: " + bookingUpdateStatusRequest.getBookingId());
        }
    }

    public List<ReservationRequest>  FilterReservationsByStatus(Status status, User currentUser) {
        ArrayList<ReservationRequest> reservationsRequest = new ArrayList<>(List.of());
        List<Booking> bookings;
        if (currentUser.getRole() == Role.CLIENT) {
            bookings = bookingRepo.findByStatusAndUser(currentUser.getId(), status);
        } else {
            bookings = bookingRepo.findByStatusAndProvider(currentUser.getId(), status);
        }
        for (Booking book : bookings) {
            ReservationRequest reservationRequest = getReservationRequest(book);
            reservationsRequest.add(reservationRequest);
        }
        return reservationsRequest;
    }

}