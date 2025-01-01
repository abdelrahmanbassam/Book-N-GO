package com.example.book_n_go.controller;

import com.example.book_n_go.model.*;
import com.example.book_n_go.enums.Day;
import com.example.book_n_go.enums.Permission;
import com.example.book_n_go.enums.Role;
import com.example.book_n_go.repository.WorkdayRepo;
import com.example.book_n_go.repository.WorkspaceRepo;
import com.example.book_n_go.service.AuthService;

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

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class WorkdayControllerTest {

	@InjectMocks
	private WorkdayController workdayController;

	@Mock
	private WorkdayRepo workdayRepo;

	@Mock
	private WorkspaceRepo workspaceRepo;

	private Workspace workspace;
	private User provider;
	private User client;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		// Mock workspace and provider
		provider = new User(1L, "user@example.com", "password", "John Doe", "123456789", Role.PROVIDER);
		client = new User(2L, "client@example.com", "password", "Jane Doe", "987654321", Role.CLIENT);
		workspace = new Workspace();
		workspace.setId(1L);
		workspace.setProvider(provider);
		workspace.setName("Workspace 1");
		// Set up SecurityContext with a mock Authentication
		Authentication authentication = mock(Authentication.class);
		when(authentication.getPrincipal()).thenReturn(provider);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
	}

	@Test
	void testGetWorkdays_ReturnsListOfWorkdays() {
		Workday workday = new Workday(1L, LocalDateTime.of(2021, 9, 6, 9, 0), LocalDateTime.of(2021, 9, 6, 17, 0), Day.MONDAY, workspace);
		when(workspaceRepo.findById(1L)).thenReturn(Optional.of(workspace));
		when(workdayRepo.findByWorkspace(workspace)).thenReturn(Collections.singletonList(workday));

		ResponseEntity<List<Workday>> response = workdayController.getWorkdays(1L);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(1, response.getBody().size());
		assertEquals(workday, response.getBody().get(0));
	}

	@Test
	void testGetWorkdays_ReturnsNoContent() {
		when(workspaceRepo.findById(1L)).thenReturn(Optional.of(workspace));
		when(workdayRepo.findByWorkspace(workspace)).thenReturn(Collections.emptyList());

		ResponseEntity<List<Workday>> response = workdayController.getWorkdays(1L);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	@Test
	void testGetWorkdays_InternalServerError() {
		when(workspaceRepo.findById(1L)).thenThrow(RuntimeException.class);

		ResponseEntity<List<Workday>> response = workdayController.getWorkdays(1L);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	void testGetWorkdayById_ReturnsWorkday() {
		Workday workday = new Workday(1L, LocalDateTime.of(2021, 9, 6, 9, 0), LocalDateTime.of(2021, 9, 6, 17, 0), Day.MONDAY, workspace);
		when(workdayRepo.findById(1L)).thenReturn(Optional.of(workday));

		ResponseEntity<Workday> response = workdayController.getWorkdayById(1L);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(workday, response.getBody());
	}

	@Test
	void testGetWorkdayById_NotFound() {
		when(workdayRepo.findById(1L)).thenReturn(Optional.empty());

		ResponseEntity<Workday> response = workdayController.getWorkdayById(1L);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void testCreateWorkday_ReturnsCreatedWorkday() {
		// Mock the Authentication and SecurityContext
		Authentication authentication = mock(Authentication.class);
		when(authentication.getPrincipal()).thenReturn(provider);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Workday workday = new Workday();
		workday.setStartTime(LocalDateTime.of(2021, 9, 6, 9, 0));
		workday.setEndTime(LocalDateTime.of(2021, 9, 6, 17, 0));
		workday.setWeekDay(Day.TUESDAY);

		when(workspaceRepo.findById(1L)).thenReturn(Optional.of(workspace));
		when(AuthService.getRequestUser()).thenReturn(provider);
		when(workdayRepo.save(any(Workday.class))).thenAnswer(i -> i.getArgument(0));

		ResponseEntity<Workday> response = workdayController.createWorkday(workday, 1L);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(workspace, response.getBody().getWorkspace());
	}

	@Test
	void testCreateWorkday_Unauthorized() {
		User otherUser = new User();
		otherUser.setId(2L);

		when(workspaceRepo.findById(1L)).thenReturn(Optional.of(workspace));
		when(AuthService.getRequestUser()).thenReturn(otherUser);

		ResponseEntity<Workday> response = workdayController.createWorkday(new Workday(), 1L);

		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
	}

	@Test
	void testUpdateWorkday_ReturnsUpdatedWorkday() {
		Workday workday = new Workday(1L, LocalDateTime.of(2021, 9, 6, 9, 0), LocalDateTime.of(2021, 9, 6, 17, 0), Day.MONDAY, workspace);
		when(workdayRepo.findById(1L)).thenReturn(Optional.of(workday));
		when(workdayRepo.save(any(Workday.class))).thenAnswer(i -> i.getArgument(0));

		workday.setStartTime(LocalDateTime.of(2021, 9, 6, 10, 0));
		ResponseEntity<Workday> response = workdayController.updateWorkday(1L, workday);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(LocalDateTime.of(2021, 9, 6, 10, 0), response.getBody().getStartTime());
	}

	@Test
	void testUpdateWorkday_Unauthorized() {
		Workday workday = new Workday(1L, LocalDateTime.of(2021, 9, 6, 9, 0), LocalDateTime.of(2021, 9, 6, 17, 0), Day.MONDAY, workspace);
		when(workdayRepo.findById(1L)).thenReturn(Optional.of(workday));
		when(AuthService.getRequestUser()).thenReturn(client);

		ResponseEntity<Workday> response = workdayController.updateWorkday(1L, workday);

		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
	}

	@Test
	void testUpdateWorkday_NotFound() {
		when(workdayRepo.findById(1L)).thenReturn(Optional.empty());
		Workday neWworkday = new Workday(1L, LocalDateTime.of(2021, 9, 6, 9, 0), LocalDateTime.of(2021, 9, 6, 17, 0), Day.MONDAY, workspace);
		ResponseEntity<Workday> response = workdayController.updateWorkday(1L, neWworkday);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void testDeleteWorkday_InternalServerError() {
		doThrow(RuntimeException.class).when(workdayRepo).deleteById(1L);

		ResponseEntity<HttpStatus> response = workdayController.deleteWorkday(1L);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	void testUpdateWorkdays_UpdatesExistingWorkdays() {
		List<Workday> existingWorkdays = Arrays.asList(
				new Workday(1L, LocalDateTime.of(2021, 9, 6, 9, 0), LocalDateTime.of(2021, 9, 6, 17, 0), Day.MONDAY, workspace),
				new Workday(2L, LocalDateTime.of(2021, 9, 7, 10, 0), LocalDateTime.of(2021, 9, 7, 18, 0), Day.TUESDAY, workspace));

		List<Workday> updatedWorkdays = Arrays.asList(
				new Workday(1L, LocalDateTime.of(2021, 9, 6, 9, 0), LocalDateTime.of(2021, 9, 6, 17, 0), Day.MONDAY, workspace),
				new Workday(2L, LocalDateTime.of(2021, 9, 7, 10, 0), LocalDateTime.of(2021, 9, 7, 18, 0), Day.TUESDAY, workspace));

		when(workspaceRepo.findById(1L)).thenReturn(Optional.of(workspace));
		when(workdayRepo.findByWorkspace(workspace)).thenReturn(existingWorkdays);
		when(workdayRepo.save(any(Workday.class))).thenAnswer(invocation -> invocation.getArgument(0));

		ResponseEntity<List<Workday>> response = workdayController.updateWorkdaysByWorkspaceId(1L, updatedWorkdays);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().size());
		verify(workdayRepo, times(2)).save(any(Workday.class));
	}

	@Test
	void testUpdateWorkdays_InvalidWorkspaceId_ReturnsInternalServerError() {
		List<Workday> updatedWorkdays = Arrays.asList(
				new Workday(1L, LocalDateTime.of(2021, 9, 6, 9, 0), LocalDateTime.of(2021, 9, 6, 17, 0), Day.MONDAY, null),
				new Workday(2L, LocalDateTime.of(2021, 9, 7, 10, 0), LocalDateTime.of(2021, 9, 7, 18, 0), Day.TUESDAY, null));

		when(workspaceRepo.findById(1L)).thenReturn(Optional.empty());

		ResponseEntity<List<Workday>> response = workdayController.updateWorkdaysByWorkspaceId(1L, updatedWorkdays);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		verify(workdayRepo, never()).save(any(Workday.class));
		verify(workdayRepo, never()).delete(any(Workday.class));
	}
	@Test
	void testDeleteWorkday_Unauthorized() {
		Workday workday = new Workday(1L, LocalDateTime.of(2021, 9, 6, 9, 0), LocalDateTime.of(2021, 9, 6, 17, 0), Day.MONDAY, workspace);
		when(workdayRepo.findById(1L)).thenReturn(Optional.of(workday));
		when(AuthService.getRequestUser()).thenReturn(client);

		ResponseEntity<HttpStatus> response = workdayController.deleteWorkday(1L);

		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
	}
	@Test
	void testDeleteWorkday_Success() {
		Workday workday = new Workday(1L, LocalDateTime.of(2021, 9, 6, 9, 0), LocalDateTime.of(2021, 9, 6, 17, 0), Day.MONDAY, workspace);
		when(workdayRepo.findById(1L)).thenReturn(Optional.of(workday));
		when(AuthService.getRequestUser()).thenReturn(provider);
		doNothing().when(workdayRepo).deleteById(1L);

		ResponseEntity<HttpStatus> response = workdayController.deleteWorkday(1L);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	@Test
	void testUpdateWorkdaysByWorkspaceId_Unauthorized() {
		when(workspaceRepo.findById(1L)).thenReturn(Optional.of(workspace));
		when(AuthService.getRequestUser()).thenReturn(client);

		ResponseEntity<List<Workday>> response = workdayController.updateWorkdaysByWorkspaceId(1L, Collections.emptyList());

		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
	}

	@Test
	void testUpdateWorkdaysByWorkspaceId_InternalServerError() {
		when(workspaceRepo.findById(1L)).thenThrow(RuntimeException.class);

		ResponseEntity<List<Workday>> response = workdayController.updateWorkdaysByWorkspaceId(1L, Collections.emptyList());

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	void testUpdateWorkdaysByWorkspaceId_ReturnsUpdatedWorkdays() {
		List<Workday> existingWorkdays = Arrays.asList(
				new Workday(1L, LocalDateTime.of(2021, 9, 6, 9, 0), LocalDateTime.of(2021, 9, 6, 17, 0), Day.MONDAY, workspace)
		);
		List<Workday> updatedWorkdays = Arrays.asList(
				new Workday(1L, LocalDateTime.of(2021, 9, 6, 10, 0), LocalDateTime.of(2021, 9, 6, 18, 0), Day.MONDAY, workspace)
		);
		when(workspaceRepo.findById(1L)).thenReturn(Optional.of(workspace));
		when(workdayRepo.findByWorkspace(workspace)).thenReturn(existingWorkdays);
		when(AuthService.getRequestUser()).thenReturn(provider);
		when(workdayRepo.save(any(Workday.class))).thenAnswer(i -> i.getArgument(0));

		ResponseEntity<List<Workday>> response = workdayController.updateWorkdaysByWorkspaceId(1L, updatedWorkdays);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(1, response.getBody().size());
		assertEquals(LocalDateTime.of(2021, 9, 6, 10, 0), response.getBody().get(0).getStartTime());
	}
}
