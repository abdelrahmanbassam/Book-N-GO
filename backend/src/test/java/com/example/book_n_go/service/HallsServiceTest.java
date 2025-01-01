package com.example.book_n_go.service;

import com.example.book_n_go.dto.HallRequest;
import com.example.book_n_go.model.Aminity;
import com.example.book_n_go.model.Hall;
import com.example.book_n_go.model.Workspace;
import com.example.book_n_go.repository.AminityRepo;
import com.example.book_n_go.repository.HallRepo;
import com.example.book_n_go.repository.WorkspaceRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class HallsServiceTest {

    @Mock
    private HallRepo hallRepo;

    @Mock
    private WorkspaceRepo workspaceRepo;

    @Mock
    private AminityRepo aminityRepo;

    @InjectMocks
    private HallsService hallsService;

    private HallRequest hallRequest;
    private Workspace workspace;
    private Aminity aminity1, aminity2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup test data
        hallRequest = new HallRequest(1L, "Test Hall", 10, "Test Hall Description", 100.0, 4.5, Set.of(1L, 2L));

        workspace = new Workspace();
        workspace.setId(1L);

        aminity1 = new Aminity();
        aminity1.setId(1L);
        aminity1.setName("Projector");

        aminity2 = new Aminity();
        aminity2.setId(2L);
        aminity2.setName("Whiteboard");
    }

    @Test
    void testCreateHall() {
        // Arrange: mock repository methods
        when(workspaceRepo.findById(1L)).thenReturn(Optional.of(workspace));
        when(aminityRepo.findById(1L)).thenReturn(Optional.of(aminity1));
        when(aminityRepo.findById(2L)).thenReturn(Optional.of(aminity2));
        when(hallRepo.save(any(Hall.class))).thenReturn(new Hall());

        // Act: call the createHall method
        Hall createdHall = hallsService.createHall(hallRequest, 1L);

        // Assert: verify that the hall was created and saved
        assertNotNull(createdHall);
        verify(workspaceRepo).findById(1L);
        verify(aminityRepo).findById(1L);
        verify(aminityRepo).findById(2L);
        verify(hallRepo).save(any(Hall.class));
    }

    @Test
    void testCreateHall_withWorkspaceNotFound() {
        // Arrange: mock workspaceRepo to return empty
        when(workspaceRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert: expect an exception to be thrown
        assertThrows(RuntimeException.class, () -> hallsService.createHall(hallRequest, 1L));
    }

    @Test
    void testCreateHall_withAminityNotFound() {
        // Arrange: mock workspace and one aminity
        when(workspaceRepo.findById(1L)).thenReturn(Optional.of(workspace));
        when(aminityRepo.findById(1L)).thenReturn(Optional.of(aminity1));
        when(aminityRepo.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert: expect an exception when aminity is not found
        assertThrows(RuntimeException.class, () -> hallsService.createHall(hallRequest, 1L));
    }
}
