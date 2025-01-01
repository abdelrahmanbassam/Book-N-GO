package com.example.book_n_go.events;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import com.example.book_n_go.model.Feedback;

public class FeedbackListenerTest {

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private FeedbackListener feedbackListener;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        feedbackListener.setApplicationEventPublisher(applicationEventPublisher);
    }

    @Test
    public void testPublishFeedbackEvent() {
        Feedback feedback = mock(Feedback.class);
        feedbackListener.publishFeedbackEvent(feedback);

        ArgumentCaptor<FeedbackEvent> eventCaptor = ArgumentCaptor.forClass(FeedbackEvent.class);
        verify(applicationEventPublisher).publishEvent(eventCaptor.capture());

        FeedbackEvent capturedEvent = eventCaptor.getValue();
        assertEquals(feedback, capturedEvent.getFeedback());
    }
}