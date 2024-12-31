package com.example.book_n_go.service;

import com.example.book_n_go.dto.FeedbackRequest;
import com.example.book_n_go.model.Feedback;
import com.example.book_n_go.model.Hall;
import com.example.book_n_go.model.User;
import com.example.book_n_go.repository.FeedbackRepo;
import com.example.book_n_go.repository.HallRepo;
import com.example.book_n_go.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepo feedbackRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private HallRepo hallRepo;

    public Optional<Hall> addFeedbackAndReturnHall(Long hallId, Long userId, FeedbackRequest feedbackRequest) {
        Optional<User> userOptional = userRepo.findById(userId);
        Optional<Hall> hallOptional = hallRepo.findById(hallId);

        if (userOptional.isPresent() && hallOptional.isPresent()) {
            User user = userOptional.get();
            Hall hall = hallOptional.get();
            Feedback feedback = new Feedback();
            feedback.setUser(user);
            feedback.setHall(hall);
            feedback.setRating(feedbackRequest.getRating());
            feedback.setContent(feedbackRequest.getComment());

            feedbackRepo.save(feedback);
            return Optional.of(hall);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Feedback> editFeedback(Long hallId, Long feedbackId, FeedbackRequest feedbackRequest) {
        Optional<Feedback> feedbackOptional = feedbackRepo.findById(feedbackId);

        if (feedbackOptional.isPresent()) {
            Feedback feedback = feedbackOptional.get();
            if (feedback.getHall().getId() != hallId) {
                return Optional.empty();
            }
            feedback.setRating(feedbackRequest.getRating());
            feedback.setContent(feedbackRequest.getComment());

            feedbackRepo.save(feedback);
            return Optional.of(feedback);
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteFeedback(Long hallId, Long feedbackId) {
        Optional<Feedback> feedbackOptional = feedbackRepo.findById(feedbackId);

        if (feedbackOptional.isPresent()) {
            Feedback feedback = feedbackOptional.get();
            if (feedback.getHall().getId() != hallId) {
                return false;
            }
            feedbackRepo.delete(feedback);
            return true;
        } else {
            return false;
        }
    }
}