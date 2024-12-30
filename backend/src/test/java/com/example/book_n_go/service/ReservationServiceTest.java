package com.example.book_n_go.service;

import com.example.book_n_go.dto.BookingUpdateStatusRequest;
import com.example.book_n_go.dto.ReservationRequest;
import com.example.book_n_go.enums.Role;
import com.example.book_n_go.enums.Status;
import com.example.book_n_go.model.Booking;
import com.example.book_n_go.model.Hall;
import com.example.book_n_go.model.User;
import com.example.book_n_go.model.Workspace;
import com.example.book_n_go.repository.BookingRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private BookingRepo bookingRepo;

    @InjectMocks
    private ReservationService reservationService;

    private User client;
    private User provider;
    private Booking booking;
    private Workspace workspace;
    private Hall hall;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        client = new User();
        client.setId(1L);
        client.setRole(Role.CLIENT);

        provider = new User();
        provider.setId(2L);
        provider.setRole(Role.PROVIDER);

        workspace = new Workspace();
        workspace.setId(1L);
        workspace.setName("Test Workspace");
        hall = new Hall();
        hall.setId(1L);
        hall.setName("Test Hall");
        hall.setCapacity(10);
        hall.setDescription("Test Description");
        hall.setPricePerHour(50);

        booking = new Booking();
        booking.setId(1L);
        booking.setUser(client);
        booking.setHall(hall);
        booking.setStatus(Status.PENDING);
        booking.setStartTime(LocalDateTime.now());
        booking.setEndTime(LocalDateTime.now().plusHours(2));
        booking.setTotalCost(100);
    }

    @Test
    public void testGetAllReservations_Client() {
        when(bookingRepo.findByUserId(client.getId())).thenReturn(Arrays.asList(booking));

        List<ReservationRequest> reservations = reservationService.getAllReservations(client);

        assertEquals(1, reservations.size());
        assertEquals(booking.getId(), reservations.get(0).getId());
    }

    @Test
    public void testGetAllReservations_Provider() {
        when(bookingRepo.findByProviderId(provider.getId())).thenReturn(Arrays.asList(booking));

        List<ReservationRequest> reservations = reservationService.getAllReservations(provider);

        assertEquals(1, reservations.size());
        assertEquals(booking.getId(), reservations.get(0).getId());
    }

    @Test
    public void testUpdateBookingStatus() {
        BookingUpdateStatusRequest request = new BookingUpdateStatusRequest();
        request.setBookingId(booking.getId());
        request.setStatus(Status.CONFIRMED);

        when(bookingRepo.findById(booking.getId())).thenReturn(Optional.of(booking));

        reservationService.UpdateBookingStatus(request);

        verify(bookingRepo).save(booking);
        assertEquals(Status.CONFIRMED, booking.getStatus());
    }

    @Test
    public void testFilterReservationsByStatus_Client() {
        when(bookingRepo.findByStatusAndUser(client.getId(), Status.PENDING)).thenReturn(Arrays.asList(booking));

        List<ReservationRequest> reservations = reservationService.FilterReservationsByStatus(Status.PENDING, client);

        assertEquals(1, reservations.size());
        assertEquals(booking.getId(), reservations.get(0).getId());
    }

    @Test
    public void testFilterReservationsByStatus_Provider() {
        when(bookingRepo.findByStatusAndProvider(provider.getId(), Status.PENDING)).thenReturn(Arrays.asList(booking));

        List<ReservationRequest> reservations = reservationService.FilterReservationsByStatus(Status.PENDING, provider);

        assertEquals(1, reservations.size());
        assertEquals(booking.getId(), reservations.get(0).getId());
    }
}