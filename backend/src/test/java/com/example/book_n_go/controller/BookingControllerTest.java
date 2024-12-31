package com.example.book_n_go.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.example.book_n_go.service.BookingService;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.example.book_n_go.config.TestConfig;
// import com.example.book_n_go.config.TestSecurityConfig;
import com.example.book_n_go.dto.BookingCreateRequest;
import com.example.book_n_go.dto.BookingUpdateDurationRequest;
import com.example.book_n_go.dto.BookingUpdateStatusRequest;
import com.example.book_n_go.enums.Status;
import com.example.book_n_go.model.Booking;
import com.example.book_n_go.model.HallSchedule;
import com.example.book_n_go.service.BookingServiceTest;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BookingController.class)
@ContextConfiguration(classes = { BookingController.class })
@ExtendWith(MockitoExtension.class)
@Import(TestConfig.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private Booking booking;
    private BookingCreateRequest bookingCreateRequest;
    private BookingUpdateDurationRequest bookingUpdateDurationRequest;
    private BookingUpdateStatusRequest bookingUpdateStatusRequest;
    private HallSchedule hallSchedule;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        booking = new Booking();
        booking.setId(1L);

        bookingCreateRequest = new BookingCreateRequest();
        bookingCreateRequest.setHallId(1L);
        bookingCreateRequest.setStartTime(LocalDateTime.now());
        bookingCreateRequest.setEndTime(LocalDateTime.now().plusHours(2));

        bookingUpdateDurationRequest = new BookingUpdateDurationRequest();
        bookingUpdateDurationRequest.setBookingId(1L);
        bookingUpdateDurationRequest.setStartTime(LocalDateTime.now());
        bookingUpdateDurationRequest.setEndTime(LocalDateTime.now().plusHours(2));

        bookingUpdateStatusRequest = new BookingUpdateStatusRequest();
        bookingUpdateStatusRequest.setBookingId(1L);
        bookingUpdateStatusRequest.setStatus(Status.CONFIRMED);

        hallSchedule = new HallSchedule();
    }

    @Test
    @WithMockUser
    public void testGetAllBookings() throws Exception {
        List<Booking> bookings = Arrays.asList(booking);
        when(bookingService.getAllBookings()).thenReturn(bookings);

        mockMvc.perform(get("/bookings/all")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(bookingService).getAllBookings();
    }

    @Test
    @WithMockUser
    public void testGetBookingsByUserId() throws Exception {
        List<Booking> bookings = Arrays.asList(booking);
        when(bookingService.getBookingsByUserId(1L)).thenReturn(bookings);

        mockMvc.perform(get("/bookings/user/1")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(bookingService).getBookingsByUserId(1L);
    }

    @Test
    @WithMockUser
    public void testGetBookingsByHallId() throws Exception {
        List<Booking> bookings = Arrays.asList(booking);
        when(bookingService.getBookingsByHallId(1L)).thenReturn(bookings);

        mockMvc.perform(get("/bookings/hall/1")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(bookingService).getBookingsByHallId(1L);
    }

    @Test
    @WithMockUser
    public void testGetHallSchedules() throws Exception {
        when(bookingService.getHallSchedules(1L, LocalDateTime.now())).thenReturn(hallSchedule);

        LocalDateTime startTime = LocalDateTime.now();

        mockMvc.perform(get("/bookings/hall/1/schedule")
                .param("startTime", startTime.toString())
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(bookingService).getHallSchedules(1L, startTime);
    }

    @Test
    @WithMockUser
    public void testCreateBooking() throws Exception {
        when(bookingService.createBooking(any(BookingCreateRequest.class))).thenReturn(booking);

        mockMvc.perform(post("/bookings/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingCreateRequest))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(bookingService).createBooking(any(BookingCreateRequest.class));
    }

    @Test
    @WithMockUser
    public void testUpdateBookingDuration() throws Exception {
        when(bookingService.updateBookingDuration(any(BookingUpdateDurationRequest.class))).thenReturn(booking);

        mockMvc.perform(put("/bookings/updateDuration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingUpdateDurationRequest))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(bookingService).updateBookingDuration(any(BookingUpdateDurationRequest.class));
    }

    @Test
    @WithMockUser
    public void testUpdateBookingStatus() throws Exception {
        when(bookingService.updateBookingStatus(any(BookingUpdateStatusRequest.class))).thenReturn(booking);

        mockMvc.perform(put("/bookings/updateStatus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingUpdateStatusRequest))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(bookingService).updateBookingStatus(any(BookingUpdateStatusRequest.class));
    }

    @Test
    @WithMockUser
    public void testDeleteBooking() throws Exception {
        mockMvc.perform(delete("/bookings/delete/1")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());

        verify(bookingService).deleteBooking(1L);
    }
}
