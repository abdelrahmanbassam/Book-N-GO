package com.example.book_n_go.events;
import com.example.book_n_go.model.Feedback;
import com.example.book_n_go.model.Hall;
import com.example.book_n_go.model.Workspace;
import com.example.book_n_go.repository.HallRepo;
import com.example.book_n_go.repository.WorkspaceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Service
public class FeedbackServiceListener {

    @Autowired
    private HallRepo hallRepo;

    @Autowired
    private WorkspaceRepo workspaceRepo;

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleFeedbackEvent(FeedbackEvent feedbackEvent) {
        Feedback feedback = feedbackEvent.getFeedback();
        Hall hall = feedback.getHall();
        updateHallRating(hall);
        updateWorkspaceRating(hall.getWorkspace());
    }

    private void updateHallRating(Hall hall) {
        double averageRating = hall.getFeedbacks().stream().mapToDouble(Feedback::getRating).average().orElse(0.0);
        hall.setRating(averageRating);
        hallRepo.save(hall);
    }

    private void updateWorkspaceRating(Workspace workspace) {
        List<Hall> halls = hallRepo.findHallsByWorkspaceId(workspace.getId());
        if (halls == null || halls.isEmpty()) {
            workspace.setRating(0.0);
        } else {
            double averageRating = halls.stream().mapToDouble(Hall::getRating).average().orElse(0.0);
            workspace.setRating(averageRating);
        }
        workspaceRepo.save(workspace);
    }
}