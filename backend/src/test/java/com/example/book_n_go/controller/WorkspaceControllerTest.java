package com.example.book_n_go.controller;

import com.example.book_n_go.model.Location;
import com.example.book_n_go.model.User;
import com.example.book_n_go.model.Workspace;
import com.example.book_n_go.repository.LocationRepo;
import com.example.book_n_go.repository.WorkspaceRepo;
import com.example.book_n_go.service.AuthService;
import com.example.book_n_go.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class WorkspaceControllerTest {

    @InjectMocks
    private WorkspaceController workspaceController;

    @Mock
    private WorkspaceRepo workspaceRepo;

    @Mock
    private LocationRepo locationRepo;

    @Mock
    private AuthService authService;

    private Workspace workspace;
    private Location location;
    private User provider;

    @BeforeEach
    void setUp() {
        // Setting up the mock data for Location, User, and Workspace
        location = new Location(1L, 101, "Main Street", "Cityville");
        provider = new User(1L, "user@example.com", "password", "John Doe", "123456789", Role.PROVIDER);
        workspace = new Workspace(1L, location, provider, "Co-working Space", 4.5, "A nice place to work");
    }

    // Test for getWorkspaces
    @Test
    void testGetWorkspaces_ReturnsWorkspaces() {
        List<Workspace> workspaces = Arrays.asList(workspace);

        when(workspaceRepo.findAll()).thenReturn(workspaces);

        ResponseEntity<List<Workspace>> response = workspaceController.getWorkspaces();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    // Test for getWorkspaceById (valid)
    @Test
    void testGetWorkspaceById_ReturnsWorkspace() {
        when(workspaceRepo.findById(1L)).thenReturn(Optional.of(workspace));

        ResponseEntity<Workspace> response = workspaceController.getWorkspaceById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(workspace.getId(), response.getBody().getId());
    }

    // Test for getWorkspaceById (not found)
    @Test
    void testGetWorkspaceById_NotFound() {
        when(workspaceRepo.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Workspace> response = workspaceController.getWorkspaceById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // Test for createWorkspace (successful creation)
    @Test
    void testCreateWorkspace_CreatesWorkspace() {
        // Mock the Authentication and SecurityContext
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(provider);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authService.getRequestUser()).thenReturn(provider);
        when(locationRepo.save(workspace.getLocation())).thenReturn(location);
        when(workspaceRepo.save(workspace)).thenReturn(workspace);

        ResponseEntity<Workspace> response = workspaceController.createWorkspace(workspace);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(workspace.getName(), response.getBody().getName());
        assertEquals(workspace.getProvider(), response.getBody().getProvider());
        // Role check for provider permissions
        assertTrue(provider.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_PROVIDER")));
    }

    // Test for createWorkspace (internal server error)
    @Test
    void testCreateWorkspace_InternalServerError() {
        // Mock the Authentication and SecurityContext
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(provider);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authService.getRequestUser()).thenReturn(provider);
        when(locationRepo.save(workspace.getLocation())).thenThrow(new RuntimeException("Error"));

        ResponseEntity<Workspace> response = workspaceController.createWorkspace(workspace);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // Test for updateWorkspace (successful update)
    @Test
    void testUpdateWorkspace_UpdatesWorkspace() {
        Workspace updatedWorkspace = new Workspace(1L, location,
                provider, "Updated Workspace", 5.0, "Updated description");

        when(workspaceRepo.findById(1L)).thenReturn(Optional.of(workspace));
        when(locationRepo.findById(workspace.getLocation().getId())).thenReturn(Optional.of(location));
        when(workspaceRepo.save(workspace)).thenReturn(updatedWorkspace);

        ResponseEntity<Workspace> response = workspaceController.updateWorkspace(1L, updatedWorkspace);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Workspace", response.getBody().getName());
        assertEquals("Updated description", response.getBody().getDescription());
        // Role check for provider permissions
        assertTrue(provider.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_PROVIDER")));
    }

    // Test for updateWorkspace (not found)
    @Test
    void testUpdateWorkspace_NotFound() {
        Workspace updatedWorkspace = new Workspace(1L, location,
                provider, "Updated Workspace", 5.0, "Updated description");

        when(workspaceRepo.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Workspace> response = workspaceController.updateWorkspace(1L, updatedWorkspace);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // Test for deleteWorkspace (successful deletion)
    @Test
    void testDeleteWorkspace_DeletesWorkspace() {
        doNothing().when(workspaceRepo).deleteById(1L);

        ResponseEntity<HttpStatus> response = workspaceController.deleteWorkspace(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    // Test for deleteWorkspace (internal server error)
    @Test
    void testDeleteWorkspace_InternalServerError() {
        doThrow(new RuntimeException("Error")).when(workspaceRepo).deleteById(1L);

        ResponseEntity<HttpStatus> response = workspaceController.deleteWorkspace(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
