package com.example.book_n_go.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.example.book_n_go.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.book_n_go.model.Booking;



@Repository
public interface BookingRepo extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b WHERE b.hall.id = :hallId" +
            " AND ((b.startTime BETWEEN :start AND :end) OR (b.endTime BETWEEN :start AND :end))")
    List<Booking> findConflictingBookings(  @Param("hallId") Long hallId, 
                                            @Param("start") LocalDateTime start, 
                                            @Param("end") LocalDateTime end);

    List<Booking> findByUserId(Long userId);

    List<Booking> findByHallId(Long hallId);

    //find bookings until a certain time
    List<Booking> findByEndTimeBefore(LocalDateTime endTime);

    // get provider reservations
    // 1 get provider workspaces
    // 2 get all halls in those workspaces
    // 3 get all reservations in those halls
    @Query("SELECT b FROM Booking b JOIN b.hall h JOIN h.workspace w JOIN w.provider p WHERE p.id = :providerId")
    List<Booking> findByProviderId(@Param("providerId") Long providerId);

    // filter reservations by status for client
    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND b.status = :status")
    List<Booking> findByStatusAndUser(@Param("userId") Long userId, @Param("status") Status status);

    // filter reservations by status for provider
    @Query("SELECT b FROM Booking b JOIN b.hall h JOIN h.workspace w JOIN w.provider p WHERE p.id = :providerId AND b.status = :status")
    List<Booking> findByStatusAndProvider(@Param("providerId") Long providerId, @Param("status") Status status);
}
