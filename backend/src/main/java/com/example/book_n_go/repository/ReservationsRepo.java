package com.example.book_n_go.repository;

import com.example.book_n_go.enums.Status;
import com.example.book_n_go.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationsRepo extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long UserId);

    // get provider reservations
    // get provider workspaces
    // get all halls in those workspaces
    // get all reservations in those halls
    @Query("SELECT r FROM Reservation r JOIN r.hall h JOIN h.workspace w WHERE w.providerId = ?1 ")
    List<Reservation> findByProviderId(Long providerId);

    // filter reservations by status for client
    @Query("SELECT r FROM Reservation r WHERE r.userId = ?1 AND r.status = ?2")
    List<Reservation> findByStatusAndUser(Long userId,Status status);

    // filter reservations by status for provider
    @Query("SELECT r FROM Reservation r JOIN r.hall h JOIN h.workspace w WHERE w.providerId = ?1 AND r.status = ?2")
    List<Reservation> findByStatusAndProvider(Long providerId,Status status);

}
