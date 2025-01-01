package com.example.book_n_go.events;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.book_n_go.model.Feedback;
import com.example.book_n_go.model.Hall;
import com.example.book_n_go.model.Workspace;
import com.example.book_n_go.repository.HallRepo;
import com.example.book_n_go.repository.WorkspaceRepo;

public class FeedbackServiceListenerTest {

    @Mock
    private HallRepo hallRepo;

    @Mock
    private WorkspaceRepo workspaceRepo;

    @InjectMocks
    private FeedbackServiceListener feedbackServiceListener;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleFeedbackEvent() {
        Feedback feedback = mock(Feedback.class);
        Hall hall = mock(Hall.class);
        Workspace workspace = mock(Workspace.class);

        when(feedback.getHall()).thenReturn(hall);
        when(hall.getWorkspace()).thenReturn(workspace);
        when(hall.getFeedbacks()).thenReturn(Collections.singleton(feedback));
        when(hallRepo.findByWorkspace(workspace)).thenReturn(Collections.singletonList(hall));

        feedbackServiceListener.handleFeedbackEvent(new FeedbackEvent(this, feedback));

        verify(hallRepo).save(hall);
        verify(workspaceRepo).save(workspace);
    }

    
    @Test
    public void testUpdateWorkspaceRating_HallsIsNull() {
        Workspace workspace = new Workspace();
        when(hallRepo.findByWorkspace(workspace)).thenReturn(null);

        feedbackServiceListener.updateWorkspaceRating(workspace);

        assertEquals(0.0, workspace.getRating());
        verify(workspaceRepo).save(workspace);
    }

    @Test
    public void testUpdateWorkspaceRating_HallsIsEmpty() {
        Workspace workspace = new Workspace();
        when(hallRepo.findByWorkspace(workspace)).thenReturn(Collections.emptyList());

        feedbackServiceListener.updateWorkspaceRating(workspace);

        assertEquals(0.0, workspace.getRating());
        verify(workspaceRepo).save(workspace);
    }

    @Test
    public void testUpdateWorkspaceRating_HallsIsNotEmpty() {
        Workspace workspace = new Workspace();
        Hall hall1 = new Hall();
        hall1.setRating(4.0);
        Hall hall2 = new Hall();
        hall2.setRating(5.0);
        List<Hall> halls = List.of(hall1, hall2);
        when(hallRepo.findByWorkspace(workspace)).thenReturn(halls);

        feedbackServiceListener.updateWorkspaceRating(workspace);

        assertEquals(4.5, workspace.getRating());
        verify(workspaceRepo).save(workspace);
    }
}