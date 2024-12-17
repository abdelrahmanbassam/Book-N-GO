package com.example.book_n_go.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import com.example.book_n_go.model.Location;
import com.example.book_n_go.model.Workspace;
import com.example.book_n_go.repository.LocationRepo;
import com.example.book_n_go.repository.WorkspaceRepo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.book_n_go.config.TestConfig;
import com.example.book_n_go.enums.Role;
import com.example.book_n_go.model.Location;
import com.example.book_n_go.model.User;
import com.example.book_n_go.model.Workspace;
import com.example.book_n_go.repository.WorkspaceRepo;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(WorkspaceController.class)
@Import(TestConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public class WorkspaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkspaceRepo workspaceRepo;

    @MockBean
    private LocationRepo locationRepo;

    private Workspace workspace;
    private User user;
    private Location location;

    public void setUp() {
        location = new Location(1L, 1, "Main St", "New York");
        user = new User(1L, "ahmad@gmail.com", "password", "Ahmad", "0123456789", Role.ADMIN);
        workspace = new Workspace(101, location, user, "Hamada Space", 3.0,
                "A cozy workspace in NY, with a great view of the city. This workspace offers a comfortable and productive environment with modern amenities, high-speed internet, and a friendly community. Ideal for freelancers, remote workers");
    }

    @Test
    public void testGetWorkspaces() throws Exception {
        setUp();
        when(workspaceRepo.findAll()).thenReturn(List.of(workspace));

        mockMvc.perform(get("/workspaces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Hamada Space"))
                .andExpect(jsonPath("$[0].rating").value(3.0))
                .andExpect(jsonPath("$[0].location.city").value("New York"))
                .andExpect(jsonPath("$[0].provider.email").value("ahmad@gmail.com"));
    }

    @Test
    public void testGetWorkspacesEmpty() throws Exception {
        when(workspaceRepo.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/workspaces"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetWorkspacesInternalServerError() throws Exception {
        doThrow(new RuntimeException("Database error")).when(workspaceRepo).findAll();
        mockMvc.perform(get("/workspaces"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testGetWorkspaceById() throws Exception {
        setUp();
        when(workspaceRepo.findById(1L)).thenReturn(Optional.of(workspace));

        mockMvc.perform(get("/workspaces/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hamada Space"))
                .andExpect(jsonPath("$.rating").value(3.0))
                .andExpect(jsonPath("$.location.city").value("New York"))
                .andExpect(jsonPath("$.provider.email").value("ahmad@gmail.com"));
    }

    @Test
    public void testGetWorkspaceByIdNotFound() throws Exception {
        when(workspaceRepo.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/workspaces/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateWorkspace() throws Exception {
        setUp();
        when(workspaceRepo.save(any(Workspace.class))).thenReturn(workspace);

        mockMvc.perform(post("/workspaces")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(workspace)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Hamada Space"))
                .andExpect(jsonPath("$.rating").value(3.0))
                .andExpect(jsonPath("$.location.city").value("New York"))
                .andExpect(jsonPath("$.provider.email").value("ahmad@gmail.com"));
    }

    @Test
    public void testCreateWorkspaceInternalServerError() throws Exception {
        setUp();
        doThrow(new RuntimeException("Database error")).when(workspaceRepo).save(any(Workspace.class));
        mockMvc.perform(post("/workspaces")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(workspace)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testUpdateWorkspace() throws Exception {
        setUp();
        when(workspaceRepo.findById(1L)).thenReturn(Optional.of(workspace));
        when(workspaceRepo.save(any(Workspace.class))).thenReturn(workspace);

        mockMvc.perform(put("/workspaces/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(workspace)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.provider.email").value("ahmad@gmail.com"));
    }

    @Test
    public void testUpdateWorkspaceWithUpdateLocation() throws Exception {
        setUp();
        when(workspaceRepo.findById(1L)).thenReturn(Optional.of(workspace));
        when(workspaceRepo.save(any(Workspace.class))).thenReturn(workspace);
        Location existingLocation = new Location(201L, 123, "Street", "City");
        when(locationRepo.findById(201L)).thenReturn(Optional.of(existingLocation));
        workspace.setLocation(new Location(201L, 12, "Street", "City"));
        mockMvc.perform(put("/workspaces/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(workspace)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.provider.email").value("ahmad@gmail.com"));
        verify(locationRepo, times(1)).save(any(Location.class));
    }

    @Test
    public void testUpdateWorkspaceNotFound() throws Exception {
        setUp();
        when(workspaceRepo.findById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/workspaces/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(workspace)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteWorkspace() throws Exception {
        doNothing().when(workspaceRepo).deleteById(1L);

        mockMvc.perform(delete("/workspaces/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteWorkspaceInternalServerError() throws Exception {
        doThrow(new RuntimeException()).when(workspaceRepo).deleteById(1L);

        mockMvc.perform(delete("/workspaces/{id}", 1))
                .andExpect(status().isInternalServerError());
    }
}