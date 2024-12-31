package com.example.book_n_go.events;

import com.example.book_n_go.model.Feedback;
import org.springframework.context.ApplicationEvent;

public class FeedbackEvent extends ApplicationEvent {

    private final Feedback feedback;

    public FeedbackEvent(Object source, Feedback feedback) {
        super(source);
        this.feedback = feedback;
    }

    public Feedback getFeedback() {
        return feedback;
    }
}