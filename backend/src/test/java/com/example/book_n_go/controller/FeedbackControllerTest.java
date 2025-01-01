package com.example.book_n_go.controller;

import com.example.book_n_go.dto.FeedbackRequest;
import com.example.book_n_go.model.Hall;
import com.example.book_n_go.service.FeedbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeedbackControllerTest {

    @Mock
    private FeedbackService feedbackService;

    @InjectMocks
    private FeedbackController feedbackController;

    private FeedbackRequest feedbackRequest;
    private Hall hall;

    @BeforeEach
    public void setUp() {
        feedbackRequest = new FeedbackRequest();
        feedbackRequest.setRating(4.5);
        feedbackRequest.setComment("Great hall!");

        hall = new Hall();
        hall.setId(1L);
        hall.setName("Test Hall");
    }

    @Test
    public void testAddFeedback_Success() {
        when(feedbackService.addFeedbackAndReturnHall(1L, 1L, feedbackRequest)).thenReturn(Optional.of(hall));

        ResponseEntity<?> response = feedbackController.addFeedback(1L, 1L, feedbackRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(hall, response.getBody());
    }

    @Test
    public void testAddFeedback_NoContent() {
        when(feedbackService.addFeedbackAndReturnHall(1L, 1L, feedbackRequest)).thenReturn(Optional.empty());

        ResponseEntity<?> response = feedbackController.addFeedback(1L, 1L, feedbackRequest);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("User already has feedback on this hall", response.getBody());
    }

    @Test
    public void testEditFeedback_Success() {
        when(feedbackService.editFeedback(1L, 1L, feedbackRequest)).thenReturn(Optional.of(hall));

        ResponseEntity<?> response = feedbackController.editFeedback(1L, 1L, feedbackRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(hall, response.getBody());
    }

    @Test
    public void testEditFeedback_NoContent() {
        when(feedbackService.editFeedback(1L, 1L, feedbackRequest)).thenReturn(Optional.empty());

        ResponseEntity<?> response = feedbackController.editFeedback(1L, 1L, feedbackRequest);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Feedback not found", response.getBody());
    }

    @Test
    public void testDeleteFeedback_Success() {
        when(feedbackService.deleteFeedback(1L, 1L)).thenReturn(Optional.of(hall));

        ResponseEntity<?> response = feedbackController.deleteFeedback(1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(hall, response.getBody());
    }

    @Test
    public void testDeleteFeedback_NoContent() {
        when(feedbackService.deleteFeedback(1L, 1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = feedbackController.deleteFeedback(1L, 1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Feedback not found", response.getBody());
    }
}
