package com.example.book_n_go.service;

import com.example.book_n_go.dto.BookingUpdateStatusRequest;
import com.example.book_n_go.dto.ReservationRequest;
import com.example.book_n_go.enums.Role;
import com.example.book_n_go.enums.Status;
import com.example.book_n_go.model.Booking;
import com.example.book_n_go.model.Reservation;
import com.example.book_n_go.model.User;
import com.example.book_n_go.repository.BookingRepo;
import com.example.book_n_go.repository.ReservationsRepo;
import com.example.book_n_go.repository.UserRepo;
import org.h2.result.UpdatableRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

//    @Autowired
//    private ReservationsRepo reservationsRepo;

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private UserRepo userRepo;
    public List<ReservationRequest> getAllReservations() {
        ArrayList<ReservationRequest> reservationsRequest = new ArrayList<>(List.of());
        //tests 
        reservationsRequest.add(new ReservationRequest(1L, "Hall1", "Client1", Status.PENDING, LocalDateTime.of(2021, 12, 12, 12, 0, 0), LocalDateTime.of(2021, 12, 12, 14, 0, 0), "Description1"));
        reservationsRequest.add(new ReservationRequest(4L, "Hall5", "Client1", Status.PENDING, LocalDateTime.of(2021, 12, 12, 12, 0, 0), LocalDateTime.of(2021, 12, 12, 14, 0, 0), "Description1"));
        reservationsRequest.add(new ReservationRequest(2L, "Hall2", "Client2", Status.CONFIRMED, LocalDateTime.of(2021, 12, 12, 12, 0, 0), LocalDateTime.of(2021, 12, 12, 14, 0, 0), "Description2"));
        reservationsRequest.add(new ReservationRequest(3L, "Hall3", "Client3", Status.REJECTED, LocalDateTime.of(2021, 12, 12, 12, 0, 0), LocalDateTime.of(2021, 12, 12, 14, 0, 0), "Description3"));

        User currentUser = AuthService.getRequestUser();
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

    public List<ReservationRequest>  FilterReservationsByStatus(Status status) {
        ArrayList<ReservationRequest> reservationsRequest = new ArrayList<>(List.of());
        User currentUser = AuthService.getRequestUser();
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


//    // get client reservations
//    public List<ReservationRequest> getClientReservations(Long clientId) {
//        List<Reservation> reservations = reservationsRepo.findByUserId(clientId);
//        ArrayList<ReservationRequest> reservationsRequest = new ArrayList<>(List.of());
//        for (Reservation reservation : reservations) {
//            ReservationRequest reservationRequest = getReservationRequest(reservation);
//            reservationsRequest.add(reservationRequest);
//        }
//        // for testing
//        reservationsRequest.add(new ReservationRequest(1L, "Hall1", "Client1", Status.PENDING, Date.valueOf("2021-12-12"), Time.valueOf("12:00:00"), Time.valueOf("14:00:00"), "Description1"));
//        reservationsRequest.add(new ReservationRequest(4L, "Hall5", "Client1", Status.PENDING, Date.valueOf("2021-12-12"), Time.valueOf("12:00:00"), Time.valueOf("14:00:00"), "Description1"));
//        reservationsRequest.add(new ReservationRequest(2L, "Hall2", "Client2", Status.CONFIRMED, Date.valueOf("2021-12-12"), Time.valueOf("12:00:00"), Time.valueOf("14:00:00"), "Description2"));
//        reservationsRequest.add(new ReservationRequest(3L, "Hall3", "Client3", Status.REJECTED, Date.valueOf("2021-12-12"), Time.valueOf("12:00:00"), Time.valueOf("14:00:00"), "Description3"));
//
//        return reservationsRequest;
//    }
//    // get provider reservations
//    public ArrayList<ReservationRequest> getProviderReservations(Long providerId) {
//        List<Reservation> reservations = reservationsRepo.findByUserId(providerId);
//        ArrayList<ReservationRequest> reservationsRequest = new ArrayList<>(List.of());
//        for (Reservation reservation : reservations) {
//            ReservationRequest reservationRequest = getReservationRequest(reservation);
//            reservationsRequest.add(reservationRequest);
//        }
//        return reservationsRequest;
//    }

    // edit reservation status
//    public void editReservationStatus(Long reservationId, Status status) {
//        Optional<Reservation> optionalReservation = reservationsRepo.findById(reservationId);
//        if (optionalReservation.isPresent()) {
//            if (status != null) {
//                Reservation reservation = optionalReservation.get();
//                reservation.setStatus(status);
//                reservationsRepo.save(reservation);
//            } else {
//                throw new IllegalArgumentException("Invalid status: " );
//            }
//        } else {
//            throw new IllegalArgumentException("Reservation not found with id: " + reservationId);
//        }
//    }
//
//    // accept reservation
//    public void acceptReservation(Long reservationId) {
//        editReservationStatus(reservationId, Status.CONFIRMED);
//    }
//    // reject reservation
//    public void rejectReservation(Long reservationId) {
//        editReservationStatus(reservationId, Status.REJECTED);
//    }
//    // cancel reservation
//    public void cancelReservation(Long reservationId) {
//        editReservationStatus(reservationId, Status.CONFIRMED);
//    }
//    // filter reservations by status
//    public ArrayList<ReservationRequest> getClientReservationsByStatus(Long userId, Status status) {
//        List<Reservation> reservations = reservationsRepo.findByStatusAndUser(userId,status);
//        ArrayList<ReservationRequest> reservationsRequest = new ArrayList<>(List.of());
//        for (Reservation reservation : reservations) {
//            ReservationRequest reservationRequest = getReservationRequest(reservation);
//            reservationsRequest.add(reservationRequest);
//        }
//        return reservationsRequest;
//    }
//    // filter reservations by status for provider
//    public ArrayList<ReservationRequest> getProviderReservationsByStatus(Long providerId, Status status) {
//        List<Reservation> reservations = reservationsRepo.findByStatusAndProvider(providerId,status);
//        ArrayList<ReservationRequest> reservationsRequest = new ArrayList<>(List.of());
//        for (Reservation reservation : reservations) {
//            ReservationRequest reservationRequest = getReservationRequest(reservation);
//            reservationsRequest.add(reservationRequest);
//        }
//        return reservationsRequest;
//    }
}