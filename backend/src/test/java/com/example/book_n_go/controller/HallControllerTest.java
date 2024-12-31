package com.example.book_n_go.controller;

import com.example.book_n_go.dto.HallRequest;
import com.example.book_n_go.model.Hall;
import com.example.book_n_go.model.Workspace;
import com.example.book_n_go.repository.HallRepo;
import com.example.book_n_go.repository.WorkspaceRepo;
import com.example.book_n_go.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HallControllerTest {

    @Mock
    private HallRepo hallRepo;

    @Mock
    private WorkspaceRepo workspaceRepo;

    @Mock
    private AuthService authService;

    @InjectMocks
    private HallController hallController;

    private Workspace workspace;
    private HallRequest hall;

    @BeforeEach
    public void setUp() {
        workspace = new Workspace();
        workspace.setId(1L);
        workspace.setName("Test Workspace");
        hall = new HallRequest();
        hall.setId(1L);
        hall.setName("Test Hall");
        hall.setCapacity(10);
        hall.setDescription("Test Description");
        hall.setPricePerHour(50);
    }

    @Test
    public void testGetHalls_Success() {
        when(workspaceRepo.findById(1L)).thenReturn(Optional.of(workspace));
        Hall hallEntity = new Hall();
        hallEntity.setId(hall.getId());
        hallEntity.setName(hall.getName());
        hallEntity.setCapacity(hall.getCapacity());
        hallEntity.setDescription(hall.getDescription());
        hallEntity.setPricePerHour(hall.getPricePerHour());
        when(hallRepo.findByWorkspace(workspace)).thenReturn(List.of(hallEntity));

        ResponseEntity<List<Hall>> response = hallController.getHalls(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testGetHalls_NoContent() {
        when(workspaceRepo.findById(1L)).thenReturn(Optional.of(workspace));
        when(hallRepo.findByWorkspace(workspace)).thenReturn(Collections.emptyList());

        ResponseEntity<List<Hall>> response = hallController.getHalls(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testGetHalls_InternalError() {
        when(workspaceRepo.findById(1L)).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<List<Hall>> response = hallController.getHalls(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetHallById_Success() {
        Hall hallEntity = new Hall();
        hallEntity.setId(hall.getId());
        hallEntity.setName(hall.getName());
        hallEntity.setCapacity(hall.getCapacity());
        hallEntity.setDescription(hall.getDescription());
        hallEntity.setPricePerHour(hall.getPricePerHour());
        when(hallRepo.findById(1L)).thenReturn(Optional.of(hallEntity));

        ResponseEntity<Hall> response = hallController.getHallById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(hall.getId(), response.getBody().getId());
    }

    @Test
    public void testGetHallById_NotFound() {
        when(hallRepo.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Hall> response = hallController.getHallById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateHall_Success() {
        Hall hallEntity = new Hall();
        hallEntity.setId(hall.getId());
        hallEntity.setName(hall.getName());
        hallEntity.setCapacity(hall.getCapacity());
        hallEntity.setDescription(hall.getDescription());
        hallEntity.setPricePerHour(hall.getPricePerHour());
        when(hallRepo.findById(1L)).thenReturn(Optional.of(hallEntity));
        when(hallRepo.save(any(Hall.class))).thenReturn(hallEntity);

        hall.setCapacity(20);
        hall.setDescription("Updated description");
        ResponseEntity<Hall> response = hallController.updateHall(1L, hall);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(20, response.getBody().getCapacity());
        assertEquals("Updated description", response.getBody().getDescription());
    }

    @Test
    public void testUpdateHall_NotFound() {
        when(hallRepo.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Hall> response = hallController.updateHall(1L, hall);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteHall_Success() {
        doNothing().when(hallRepo).deleteById(1L);

        ResponseEntity<HttpStatus> response = hallController.deleteHall(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteHall_InternalError() {
        doThrow(new RuntimeException("Database error")).when(hallRepo).deleteById(1L);

        ResponseEntity<HttpStatus> response = hallController.deleteHall(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
