package com.example.book_n_go.controller;

import com.example.book_n_go.dto.HallRequest;
import com.example.book_n_go.dto.HallsFilterRequest;
import com.example.book_n_go.model.Hall;
import com.example.book_n_go.model.Workspace;
import com.example.book_n_go.repository.HallRepo;
import com.example.book_n_go.repository.WorkspaceRepo;
import com.example.book_n_go.service.AuthService;
import com.example.book_n_go.service.HallsListFilterService;

import org.springframework.data.domain.Page;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    @Mock
    private HallsListFilterService hallsListFilterService;

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

    @Test
    public void testFilterHalls_Success() {
        HallsFilterRequest request = new HallsFilterRequest();
        request.setPage(1);
        request.setPageSize(5);
        request.setAminities(Collections.singletonList("WiFi"));

        Hall hallEntity = new Hall();
        hallEntity.setId(1L);
        hallEntity.setName("Test Hall");
        hallEntity.setCapacity(10);
        hallEntity.setDescription("Test Description");
        hallEntity.setPricePerHour(50);

        Page<Hall> pageHalls = new PageImpl<>(List.of(hallEntity), PageRequest.of(0, 5), 1);

        when(hallsListFilterService.applyCriterias(request)).thenReturn(pageHalls);

        ResponseEntity<Map<String, Object>> response = hallController.filterHalls(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, ((List<Hall>) response.getBody().get("halls")).size());

        Map<String, Object> pagination = (Map<String, Object>) response.getBody().get("pagination");
        assertNotNull(pagination);
        assertEquals(1, pagination.get("currentPage"));
        assertEquals(1, pagination.get("totalPages"));
        assertEquals(5, pagination.get("pageSize"));
        assertEquals(1L, pagination.get("totalItems"));
    }

    @Test
    public void testFilterHalls_EmptyResult() {
        HallsFilterRequest request = new HallsFilterRequest();
        request.setPage(1);
        request.setPageSize(5);

        Page<Hall> pageHalls = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 5), 0);

        when(hallsListFilterService.applyCriterias(request)).thenReturn(pageHalls);

        ResponseEntity<Map<String, Object>> response = hallController.filterHalls(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, ((List<Hall>) response.getBody().get("halls")).size());

        Map<String, Object> pagination = (Map<String, Object>) response.getBody().get("pagination");
        assertNotNull(pagination);
        assertEquals(1, pagination.get("currentPage"));
        assertEquals(0, pagination.get("totalPages"));
        assertEquals(5, pagination.get("pageSize"));
        assertEquals(0L, pagination.get("totalItems"));
    }

    @Test
    public void testFilterHalls_InternalServerError() {
        HallsFilterRequest request = new HallsFilterRequest();
        request.setPage(1);
        request.setPageSize(5);

        when(hallsListFilterService.applyCriterias(request)).thenThrow(new RuntimeException("Error processing request"));

        ResponseEntity<Map<String, Object>> response = hallController.filterHalls(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
