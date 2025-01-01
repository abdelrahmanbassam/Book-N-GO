package com.example.book_n_go.controller;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.book_n_go.dto.FeedbackRequest;
import com.example.book_n_go.model.Hall;
import com.example.book_n_go.service.FeedbackService;


@RestController
@RequestMapping("/workspace/{workspaceId}/halls/{hallId}/feedback")
@CrossOrigin(origins = "http://localhost:3000")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;
   
    @PostMapping("/add")
    public ResponseEntity<?> addFeedback(@PathVariable("hallId") Long hallId, @RequestParam("userId") Long userId, @RequestBody FeedbackRequest feedbackRequest) {
        Optional<Hall> hallOptional = feedbackService.addFeedbackAndReturnHall(hallId, userId, feedbackRequest);

        if (!hallOptional.isPresent()) {
            return new ResponseEntity<>("User already has feedback on this hall", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(hallOptional.get(), HttpStatus.OK);
        
    }

    @PutMapping("/edit/{feedbackId}")
    public ResponseEntity<?> editFeedback(@PathVariable Long hallId, @PathVariable Long feedbackId, @RequestBody FeedbackRequest feedbackRequest) {
        Optional<Hall> hallOptional = feedbackService.editFeedback(hallId, feedbackId, feedbackRequest);
        if (!hallOptional.isPresent()) {
            return new ResponseEntity<>("Feedback not found", HttpStatus.NO_CONTENT);
        } 
        return new ResponseEntity<>(hallOptional.get(), HttpStatus.OK);
        
    }

    @DeleteMapping("/delete/{feedbackId}")
    public ResponseEntity<?> deleteFeedback(@PathVariable Long hallId, @PathVariable Long feedbackId) {
        Optional<Hall> hallOptional = feedbackService.deleteFeedback(hallId, feedbackId);

        if (!hallOptional.isPresent()) {
            return new ResponseEntity<>("Feedback not found", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(hallOptional.get(), HttpStatus.OK);
        
    }
    
}