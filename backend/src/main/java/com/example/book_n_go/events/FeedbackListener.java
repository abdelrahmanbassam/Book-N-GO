package com.example.book_n_go.events;

import com.example.book_n_go.model.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

@Component
public class FeedbackListener {

    private static ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        FeedbackListener.applicationEventPublisher = applicationEventPublisher;
    }

    @PostPersist
    @PostUpdate
    @PostRemove
    public void publishFeedbackEvent(Feedback feedback) {
        applicationEventPublisher.publishEvent(new FeedbackEvent(this, feedback));
    }
}