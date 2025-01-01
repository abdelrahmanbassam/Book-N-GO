package com.example.book_n_go.service;

import com.example.book_n_go.dto.FeedbackRequest;
import com.example.book_n_go.model.Feedback;
import com.example.book_n_go.model.Hall;
import com.example.book_n_go.model.User;
import com.example.book_n_go.repository.FeedbackRepo;
import com.example.book_n_go.repository.HallRepo;
import com.example.book_n_go.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeedbackServiceTest {

    @Mock
    private FeedbackRepo feedbackRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private HallRepo hallRepo;

    @InjectMocks
    private FeedbackService feedbackService;

    private FeedbackRequest feedbackRequest;
    private User user;
    private Hall hall;
    private Feedback feedback;

    @BeforeEach
    public void setUp() {
        feedbackRequest = new FeedbackRequest();
        feedbackRequest.setRating(4.5);
        feedbackRequest.setComment("Great hall!");

        user = new User();
        user.setId(1L);

        hall = new Hall();
        hall.setId(1L);
        hall.setName("Test Hall");

        feedback = new Feedback();
        feedback.setId(1L);
        feedback.setUser(user);
        feedback.setHall(hall);
        feedback.setRating(4.5);
        feedback.setContent("Great hall!");
    }

    @Test
    public void testAddFeedbackAndReturnHall_Success() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(hallRepo.findById(1L)).thenReturn(Optional.of(hall));
        when(feedbackRepo.save(any(Feedback.class))).thenReturn(feedback);

        Optional<Hall> result = feedbackService.addFeedbackAndReturnHall(1L, 1L, feedbackRequest);

        assertTrue(result.isPresent());
        assertEquals(hall.getId(), result.get().getId());
        verify(feedbackRepo, times(1)).save(any(Feedback.class));
    }

    @Test
    public void testAddFeedbackAndReturnHall_UserNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        Optional<Hall> result = feedbackService.addFeedbackAndReturnHall(1L, 1L, feedbackRequest);

        assertFalse(result.isPresent());
        verify(feedbackRepo, never()).save(any(Feedback.class));
    }

    @Test
    public void testAddFeedbackAndReturnHall_HallNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(hallRepo.findById(1L)).thenReturn(Optional.empty());

        Optional<Hall> result = feedbackService.addFeedbackAndReturnHall(1L, 1L, feedbackRequest);

        assertFalse(result.isPresent());
        verify(feedbackRepo, never()).save(any(Feedback.class));
    }

    @Test
    public void testEditFeedback_Success() {
        when(feedbackRepo.findById(1L)).thenReturn(Optional.of(feedback));
        feedbackRequest.setRating(5.0);
        feedbackRequest.setComment("Updated comment");

        Optional<Hall> result = feedbackService.editFeedback(1L, 1L, feedbackRequest);

        assertTrue(result.isPresent());
        assertEquals(hall.getId(), result.get().getId());
        assertEquals(5.0, feedback.getRating());
        assertEquals("Updated comment", feedback.getContent());
        verify(feedbackRepo, times(1)).save(any(Feedback.class));
    }

    @Test
    public void testEditFeedback_FeedbackNotFound() {
        when(feedbackRepo.findById(1L)).thenReturn(Optional.empty());

        Optional<Hall> result = feedbackService.editFeedback(1L, 1L, feedbackRequest);

        assertFalse(result.isPresent());
        verify(feedbackRepo, never()).save(any(Feedback.class));
    }

    @Test
    public void testEditFeedback_HallMismatch() {
        feedback.getHall().setId(2L);
        when(feedbackRepo.findById(1L)).thenReturn(Optional.of(feedback));

        Optional<Hall> result = feedbackService.editFeedback(1L, 1L, feedbackRequest);

        assertFalse(result.isPresent());
        verify(feedbackRepo, never()).save(any(Feedback.class));
    }

    @Test
    public void testDeleteFeedback_Success() {
        when(feedbackRepo.findById(1L)).thenReturn(Optional.of(feedback));

        Optional<Hall> result = feedbackService.deleteFeedback(1L, 1L);

        assertTrue(result.isPresent());
        assertEquals(hall.getId(), result.get().getId());
        verify(feedbackRepo, times(1)).delete(any(Feedback.class));
    }

    @Test
    public void testDeleteFeedback_FeedbackNotFound() {
        when(feedbackRepo.findById(1L)).thenReturn(Optional.empty());

        Optional<Hall> result = feedbackService.deleteFeedback(1L, 1L);

        assertFalse(result.isPresent());
        verify(feedbackRepo, never()).delete(any(Feedback.class));
    }

    @Test
    public void testDeleteFeedback_HallMismatch() {
        feedback.getHall().setId(2L);
        when(feedbackRepo.findById(1L)).thenReturn(Optional.of(feedback));

        Optional<Hall> result = feedbackService.deleteFeedback(1L, 1L);

        assertFalse(result.isPresent());
        verify(feedbackRepo, never()).delete(any(Feedback.class));
    }
}
