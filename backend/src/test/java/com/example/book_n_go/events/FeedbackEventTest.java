package com.example.book_n_go.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.book_n_go.model.Feedback;

public class FeedbackEventTest {

    @Test
    public void testFeedbackEvent() {
        Feedback feedback = new Feedback();
        FeedbackEvent feedbackEvent = new FeedbackEvent(this, feedback);

        assertEquals(feedback, feedbackEvent.getFeedback());
    }
}