package com.example.book_n_go.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.book_n_go.enums.Permission;
import com.example.book_n_go.enums.Role;
import com.example.book_n_go.model.Location;
import com.example.book_n_go.model.User;
import com.example.book_n_go.repository.LocationRepo;
import com.example.book_n_go.service.AuthService;

class LocationControllerTest {

    @InjectMocks
    private LocationController locationController;

    @Mock
    private LocationRepo locationRepo;

    @Mock
    private AuthService authService;

    private Location location;
    private User provider;
    private User admin;
    private User client;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        provider = new User(1L, "user@example.com", "password", "John Doe", "123456789", Role.PROVIDER);
        admin = new User(2L, "admin@example.com", "password", "Jane Doe", "987654321", Role.ADMIN);
        client = new User(3L, "client@example.com", "password", "Alice Doe", "456789123", Role.CLIENT);
        location = new Location();
        location.setId(1L);
        location.setCity("New York");

        // Set up SecurityContext with a mock Authentication
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(provider);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetAllLocations_ReturnsListOfLocations() {
        Location location = new Location(1, 101, "200", "New York");
        when(locationRepo.findAll()).thenReturn(Collections.singletonList(location));

        ResponseEntity<List<Location>> response = locationController.getAllLocations();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(location, response.getBody().get(0));
    }

    @Test
    void testGetAllLocations_EmptyList() {
        when(locationRepo.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Location>> response = locationController.getAllLocations();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testGetAllLocations_InternalServerError() {
        when(locationRepo.findAll()).thenThrow(RuntimeException.class);

        ResponseEntity<List<Location>> response = locationController.getAllLocations();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetLocationById_ReturnsLocation() {
        Location location = new Location(1, 101, "200", "New York");
        when(locationRepo.findById(1L)).thenReturn(Optional.of(location));

        ResponseEntity<Location> response = locationController.getLocationById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(location, response.getBody());
    }

    @Test
    void testGetLocationById_NotFound() {
        when(locationRepo.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Location> response = locationController.getLocationById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateLocation_ReturnsCreatedLocation() {
        // Mock the Authentication and SecurityContext
		Authentication authentication = mock(Authentication.class);
		when(authentication.getPrincipal()).thenReturn(admin);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
        when(AuthService.getRequestUser()).thenReturn(admin);
        Location location = new Location(1, 101, "200", "New York");
        when(locationRepo.save(any(Location.class))).thenAnswer(i -> i.getArgument(0));

        ResponseEntity<Location> response = locationController.createLocation(location);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(location, response.getBody());
    }

    @Test
    void testCreateLocation_Unauthorized() {
        // Mock the Authentication and SecurityContext
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(client);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(AuthService.getRequestUser()).thenReturn(client);
        Location location = new Location(1, 101, "200", "New York");

        ResponseEntity<Location> response = locationController.createLocation(location);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    }

    @Test
    void testCreateLocation_InternalServerError() {
        Location location = new Location(1, 101, "200", "New York");
        when(locationRepo.save(any(Location.class))).thenThrow(RuntimeException.class);

        ResponseEntity<Location> response = locationController.createLocation(location);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testUpdateLocation_ReturnsUpdatedLocation() {
        // Mock the Authentication and SecurityContext
		Authentication authentication = mock(Authentication.class);
		when(authentication.getPrincipal()).thenReturn(admin);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
        when(AuthService.getRequestUser()).thenReturn(admin);
        Location location = new Location(1, 101, "200", "New York");
        when(locationRepo.findById(1L)).thenReturn(Optional.of(location));
        when(locationRepo.save(any(Location.class))).thenAnswer(i -> i.getArgument(0));

        location.setCity("Los Angeles");
        ResponseEntity<Location> response = locationController.updateLocation(1L, location);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Los Angeles", response.getBody().getCity());
    }

    @Test
    void testUpdateLocation_Unauthorized() {
        // Mock the Authentication and SecurityContext
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(client);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(AuthService.getRequestUser()).thenReturn(client);
        Location location = new Location(1, 101, "200", "New York");
        when(locationRepo.findById(1L)).thenReturn(Optional.of(location));

        ResponseEntity<Location> response = locationController.updateLocation(1L, location);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }


    @Test
    void testUpdateLocation_NotFound() {
        when(locationRepo.findById(1L)).thenReturn(Optional.empty());

        Location location = new Location(1, 101, "200", "New York");
        ResponseEntity<Location> response = locationController.updateLocation(1L, location);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteLocation_Unauthorized() {
        // Mock the Authentication and SecurityContext
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(client);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(AuthService.getRequestUser()).thenReturn(client);

        ResponseEntity<HttpStatus> response = locationController.deleteLocation(1L);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}