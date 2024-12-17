package com.example.book_n_go.service;

import com.example.book_n_go.dto.ReservationRequest;
import com.example.book_n_go.enums.Status;
import com.example.book_n_go.model.Reservation;
import com.example.book_n_go.repository.ReservationsRepo;
import com.example.book_n_go.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationsRepo reservationsRepo;

    @Autowired
    private UserRepository userRepository;

    // get client reservations
    public List<ReservationRequest> getClientReservations(Long clientId) {
        List<Reservation> reservations = reservationsRepo.findByUserId(clientId);
        ArrayList<ReservationRequest> reservationsRequest = new ArrayList<>(List.of());
        for (Reservation reservation : reservations) {
            ReservationRequest reservationRequest = getReservationRequest(reservation);
            reservationsRequest.add(reservationRequest);
        }
        // for testing
        reservationsRequest.add(new ReservationRequest(1L, "Hall1", "Client1", Status.PENDING, Date.valueOf("2021-12-12"), Time.valueOf("12:00:00"), Time.valueOf("14:00:00"), "Description1"));
        reservationsRequest.add(new ReservationRequest(4L, "Hall5", "Client1", Status.PENDING, Date.valueOf("2021-12-12"), Time.valueOf("12:00:00"), Time.valueOf("14:00:00"), "Description1"));
        reservationsRequest.add(new ReservationRequest(2L, "Hall2", "Client2", Status.ACCEPTED, Date.valueOf("2021-12-12"), Time.valueOf("12:00:00"), Time.valueOf("14:00:00"), "Description2"));
        reservationsRequest.add(new ReservationRequest(3L, "Hall3", "Client3", Status.REJECTED, Date.valueOf("2021-12-12"), Time.valueOf("12:00:00"), Time.valueOf("14:00:00"), "Description3"));

        return reservationsRequest;
    }

    private ReservationRequest getReservationRequest(Reservation reservation) {
        ReservationRequest reservationRequest = new ReservationRequest();
        reservationRequest.setId(reservation.getId());
        reservationRequest.setHallName(reservation.getHall().getName());
        reservationRequest.setClientName(reservation.getUser().getName());
        reservationRequest.setStatus(reservation.getStatus());
        reservationRequest.setDate(reservation.getDate());
        reservationRequest.setStartTime(reservation.getStartTime());
        reservationRequest.setEndTime(reservation.getEndTime());
        reservationRequest.setHallDescription(reservation.getHall().getDescription());
        return reservationRequest;
    }

    // get provider reservations
    public ArrayList<ReservationRequest> getProviderReservations(Long providerId) {
        List<Reservation> reservations = reservationsRepo.findByUserId(providerId);
        ArrayList<ReservationRequest> reservationsRequest = new ArrayList<>(List.of());
        for (Reservation reservation : reservations) {
            ReservationRequest reservationRequest = getReservationRequest(reservation);
            reservationsRequest.add(reservationRequest);
        }
        return reservationsRequest;
    }

    // edit reservation status
    public void editReservationStatus(Long reservationId, Status status) {
        Optional<Reservation> optionalReservation = reservationsRepo.findById(reservationId);
        if (optionalReservation.isPresent()) {
            if (status != null) {
                Reservation reservation = optionalReservation.get();
                reservation.setStatus(status);
                reservationsRepo.save(reservation);
            } else {
                throw new IllegalArgumentException("Invalid status: " );
            }
        } else {
            throw new IllegalArgumentException("Reservation not found with id: " + reservationId);
        }
    }

    // accept reservation
    public void acceptReservation(Long reservationId) {
        editReservationStatus(reservationId, Status.ACCEPTED);
    }
    // reject reservation
    public void rejectReservation(Long reservationId) {
        editReservationStatus(reservationId, Status.REJECTED);
    }
    // cancel reservation
    public void cancelReservation(Long reservationId) {
        editReservationStatus(reservationId, Status.CANCELLED);
    }
    // filter reservations by status
    public ArrayList<ReservationRequest> getClientReservationsByStatus(Long userId, Status status) {
        List<Reservation> reservations = reservationsRepo.findByStatusAndUser(userId,status);
        ArrayList<ReservationRequest> reservationsRequest = new ArrayList<>(List.of());
        for (Reservation reservation : reservations) {
            ReservationRequest reservationRequest = getReservationRequest(reservation);
            reservationsRequest.add(reservationRequest);
        }
        return reservationsRequest;

    }
    // filter reservations by status for provider
    public ArrayList<ReservationRequest> getProviderReservationsByStatus(Long providerId, Status status) {
        List<Reservation> reservations = reservationsRepo.findByStatusAndProvider(providerId,status);
        ArrayList<ReservationRequest> reservationsRequest = new ArrayList<>(List.of());
        for (Reservation reservation : reservations) {
            ReservationRequest reservationRequest = getReservationRequest(reservation);
            reservationsRequest.add(reservationRequest);
        }
        return reservationsRequest;
    }
}