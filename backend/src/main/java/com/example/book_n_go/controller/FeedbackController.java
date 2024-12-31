package com.example.book_n_go.controller;

import com.example.book_n_go.dto.FeedbackRequest;
import com.example.book_n_go.model.Feedback;
import com.example.book_n_go.model.Hall;
import com.example.book_n_go.service.FeedbackService;

import org.apache.poi.sl.draw.geom.GuideIf.Op;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/workspace/{workspaceId}/halls/{hallId}/feedback")
@CrossOrigin(origins = "http://localhost:3000")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;
   
    @PostMapping("/add")
    public ResponseEntity<Hall> addFeedback(@PathVariable Long hallId,  @RequestParam Long userId, @RequestBody FeedbackRequest feedbackRequest) {
        Optional<Hall> hallOptional = feedbackService.addFeedbackAndReturnHall(hallId, userId, feedbackRequest);
        return hallOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/edit/{feedbackId}")
    public ResponseEntity<Feedback> editFeedback(@PathVariable Long hallId, @PathVariable Long feedbackId, @RequestBody FeedbackRequest feedbackRequest) {
        Optional<Feedback> feedbackOptional = feedbackService.editFeedback(hallId, feedbackId, feedbackRequest);
        return feedbackOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{feedbackId}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long hallId, @PathVariable Long feedbackId) {
        boolean deleted = feedbackService.deleteFeedback(hallId, feedbackId);

        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}