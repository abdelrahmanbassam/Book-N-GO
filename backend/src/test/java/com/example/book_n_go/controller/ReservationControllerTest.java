package com.example.book_n_go.controller;

import com.example.book_n_go.dto.BookingUpdateStatusRequest;
import com.example.book_n_go.dto.ReservationRequest;
import com.example.book_n_go.enums.Status;
import com.example.book_n_go.model.User;
import com.example.book_n_go.service.AuthService;
import com.example.book_n_go.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private ReservationController reservationController;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private User user;
    private ReservationRequest reservationRequest;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);

        reservationRequest = new ReservationRequest();
        reservationRequest.setId(1L);


    }

    @Test
    public void testGetAllReservations() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);

        when(authService.getRequestUser()).thenReturn(user);
        when(reservationService.getAllReservations(user)).thenReturn(Arrays.asList(reservationRequest));

        ResponseEntity<List<ReservationRequest>> response = reservationController.getAllReservations();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(reservationRequest.getId(), response.getBody().get(0).getId());
    }

    @Test
    public void testUpdateStatus() {
        BookingUpdateStatusRequest request = new BookingUpdateStatusRequest();
        request.setBookingId(1L);
        request.setStatus(Status.CONFIRMED);

        doNothing().when(reservationService).UpdateBookingStatus(request);

        ResponseEntity<String> response = reservationController.updateStatus(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(reservationService, times(1)).UpdateBookingStatus(request);
    }

    @Test
    public void testGetReservationsByStatus() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);
        when(authService.getRequestUser()).thenReturn(user);
        when(reservationService.FilterReservationsByStatus(Status.PENDING, user)).thenReturn(Arrays.asList(reservationRequest));

        ResponseEntity<List<ReservationRequest>> response = reservationController.getReservationsByStatus(Status.PENDING);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(reservationRequest.getId(), response.getBody().get(0).getId());
    }

    @Test
    public void testGetAllReservations_EmptyList() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);
        when(authService.getRequestUser()).thenReturn(user);
        when(reservationService.getAllReservations(user)).thenReturn(Arrays.asList());

        ResponseEntity<List<ReservationRequest>> response = reservationController.getAllReservations();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    public void testGetAllReservations_Exception() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);
        when(authService.getRequestUser()).thenReturn(user);
        when(reservationService.getAllReservations(user)).thenThrow(new RuntimeException("Service exception"));

        ResponseEntity<List<ReservationRequest>> response = reservationController.getAllReservations();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateStatus_InvalidStatus() {
        BookingUpdateStatusRequest request = new BookingUpdateStatusRequest();
        request.setBookingId(1L);
        request.setStatus(null); // Invalid status

        ResponseEntity<String> response = reservationController.updateStatus(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


}