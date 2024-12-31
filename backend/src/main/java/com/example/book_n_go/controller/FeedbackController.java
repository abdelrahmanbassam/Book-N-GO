package com.example.book_n_go.controller;
import com.example.book_n_go.dto.FeedbackRequest;
import com.example.book_n_go.model.Hall;
import com.example.book_n_go.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/workspace/{workspaceId}/halls/{hallId}/feedback")
@CrossOrigin(origins = "http://localhost:3000")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;
   
    @PostMapping("/add")
    public ResponseEntity<?> addFeedback(@PathVariable Long hallId, @RequestParam Long userId, @RequestBody FeedbackRequest feedbackRequest) {
        Optional<Hall> hallOptional = feedbackService.addFeedbackAndReturnHall(hallId, userId, feedbackRequest);

        if (hallOptional.isPresent()) {
            return new ResponseEntity<>(hallOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User already has feedback on this hall", HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/edit/{feedbackId}")
    public ResponseEntity<?> editFeedback(@PathVariable Long hallId, @PathVariable Long feedbackId, @RequestBody FeedbackRequest feedbackRequest) {
        Optional<Hall> hallOptional = feedbackService.editFeedback(hallId, feedbackId, feedbackRequest);
        if (hallOptional.isPresent()) {
            return new ResponseEntity<>(hallOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Feedback not found", HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping("/delete/{feedbackId}")
    public ResponseEntity<?> deleteFeedback(@PathVariable Long hallId, @PathVariable Long feedbackId) {
        Optional<Hall> hallOptional = feedbackService.deleteFeedback(hallId, feedbackId);

        if (hallOptional.isPresent()) {
            return new ResponseEntity<>(hallOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Feedback not found", HttpStatus.NO_CONTENT);
        }
    }
    @GetMapping("/greeting")
    public ResponseEntity<String> greeting() {
        return new ResponseEntity<>("Hello, welcome to the Feedback API!", HttpStatus.OK);
    }
}