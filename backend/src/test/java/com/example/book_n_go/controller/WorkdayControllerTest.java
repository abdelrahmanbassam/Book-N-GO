package com.example.book_n_go.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.example.book_n_go.enums.Day;
import com.example.book_n_go.model.Workday;
import com.example.book_n_go.repository.WorkdayRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

@WebMvcTest(WorkdayController.class)
@AutoConfigureMockMvc(addFilters = false)
public class WorkdayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkdayRepo workdayRepo;

    private Workday workday;

    @BeforeEach
    public void setUp() {
        workday = new Workday(1L, 101, null, Time.valueOf("09:00:00"), Time.valueOf("17:00:00"), Day.MONDAY);
    }

    @Test
    public void testGetAllWorkdays() throws Exception {
        when(workdayRepo.findAll()).thenReturn(List.of(workday));

        mockMvc.perform(get("/workdays"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].startTime").value("09:00:00"))
                .andExpect(jsonPath("$[0].endTime").value("17:00:00"));
    }

    @Test
    public void testGetAllWorkdaysEmpty() throws Exception {
        when(workdayRepo.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/workdays"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetAllWorkdaysInternalServerError() throws Exception {
        doThrow(new RuntimeException("Database error")).when(workdayRepo).findAll();

        mockMvc.perform(get("/workdays"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testGetWorkdayById() throws Exception {
        when(workdayRepo.findById(1L)).thenReturn(Optional.of(workday));

        mockMvc.perform(get("/workdays/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startTime").value("09:00:00"))
                .andExpect(jsonPath("$.endTime").value("17:00:00"));
    }

    @Test
    public void testGetWorkdayByIdNotFound() throws Exception {
        when(workdayRepo.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/workdays/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetWorkdaysByWorkspaceId() throws Exception {
        when(workdayRepo.findByWorkspaceId(101)).thenReturn(List.of(workday));

        mockMvc.perform(get("/workspaces/{workspaceId}/workdays", 101))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].startTime").value("09:00:00"))
                .andExpect(jsonPath("$[0].endTime").value("17:00:00"));
    }

    @Test
    public void testGetWorkdaysByWorkspaceIdEmpty() throws Exception {
        when(workdayRepo.findByWorkspaceId(101)).thenReturn(List.of());

        mockMvc.perform(get("/workspaces/{workspaceId}/workdays", 101))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetWorkdaysByWorkspaceIdInternalServerError() throws Exception {
        doThrow(new RuntimeException("Database error")).when(workdayRepo).findByWorkspaceId(101);

        mockMvc.perform(get("/workspaces/{workspaceId}/workdays", 101))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testCreateWorkday() throws Exception {
        when(workdayRepo.save(any(Workday.class))).thenReturn(workday);

        mockMvc.perform(post("/workdays")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(workday)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.startTime").value("09:00:00"))
                .andExpect(jsonPath("$.endTime").value("17:00:00"));
    }

    @Test
    public void testCreateWorkdayInternalServerError() throws Exception {
        doThrow(new RuntimeException("Database error")).when(workdayRepo).save(any(Workday.class));

        mockMvc.perform(post("/workdays")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(workday)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testUpdateWorkday() throws Exception {
        when(workdayRepo.findById(1L)).thenReturn(Optional.of(workday));
        when(workdayRepo.save(any(Workday.class))).thenReturn(workday);

        workday.setStartTime(Time.valueOf("10:00:00"));
        workday.setEndTime(Time.valueOf("18:00:00"));

        mockMvc.perform(put("/workdays/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(workday)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startTime").value("10:00:00"))
                .andExpect(jsonPath("$.endTime").value("18:00:00"));
    }

    @Test
    public void testUpdateWorkdayNotFound() throws Exception {
        when(workdayRepo.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/workdays/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(workday)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateWorkdayInternalServerError() throws Exception {
        when(workdayRepo.findById(1L)).thenReturn(Optional.of(workday));
        doThrow(new RuntimeException("Database error")).when(workdayRepo).save(any(Workday.class));
    }

    @Test
    public void testUpdateWorkdaysByWorkspaceId() throws Exception {
        when(workdayRepo.findByWorkspaceId(101)).thenReturn(List.of(workday));
        when(workdayRepo.saveAll(any())).thenReturn(List.of(workday));

        mockMvc.perform(put("/workspaces/{workspaceId}/workdays", 101)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(List.of(workday))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].startTime").value("09:00:00"))
                .andExpect(jsonPath("$[0].endTime").value("17:00:00"));
    }

    @Test
    public void testUpdateWorkdaysByWorkspaceIdUpdateAndDelete() throws Exception {
        Workday existingWorkday = new Workday(1L, 101, null, Time.valueOf("09:00:00"), Time.valueOf("17:00:00"),
                Day.MONDAY);
        Workday updatedWorkday = new Workday(1L, 101, null, Time.valueOf("10:00:00"), Time.valueOf("18:00:00"),
                Day.MONDAY);

        when(workdayRepo.findByWorkspaceId(101L)).thenReturn(List.of(existingWorkday));

        mockMvc.perform(put("/workspaces/{workspaceId}/workdays", 101L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(List.of(updatedWorkday))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].startTime").value("10:00:00"))
                .andExpect(jsonPath("$[0].endTime").value("18:00:00"));
        verify(workdayRepo).findByWorkspaceId(101L);
        verify(workdayRepo).save(argThat(workday -> (workday.getId() == updatedWorkday.getId()) &&
                workday.getWorkspaceId() == 101 &&
                workday.getStartTime().equals(Time.valueOf("10:00:00")) &&
                workday.getEndTime().equals(Time.valueOf("18:00:00")) &&
                workday.getWeekDay() == Day.MONDAY));
        verify(workdayRepo, never()).delete(existingWorkday);
    }

    @Test
    public void testUpdateWorkdaysByWorkspaceIdAddNewWorkday() throws Exception {
        Workday newWorkday = new Workday(2L, 101, null, Time.valueOf("09:00:00"), Time.valueOf("17:00:00"),
                Day.TUESDAY);

        when(workdayRepo.findByWorkspaceId(101L)).thenReturn(List.of());

        mockMvc.perform(put("/workspaces/{workspaceId}/workdays", 101L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(List.of(newWorkday))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].weekDay").value("TUESDAY"));
        verify(workdayRepo).findByWorkspaceId(101L);
        verify(workdayRepo).save(argThat(workday -> (workday.getId() == newWorkday.getId()) &&
                workday.getWorkspaceId() == 101 &&
                workday.getStartTime().equals(Time.valueOf("09:00:00")) &&
                workday.getEndTime().equals(Time.valueOf("17:00:00")) &&
                workday.getWeekDay() == Day.TUESDAY));
    }

    @Test
    public void testUpdateWorkdaysByWorkspaceIdDeleteWorkday() throws Exception {
        Workday existingWorkday = new Workday(1L, 101, null, Time.valueOf("09:00:00"), Time.valueOf("17:00:00"),
                Day.MONDAY);

        when(workdayRepo.findByWorkspaceId(101L)).thenReturn(List.of(existingWorkday));

        mockMvc.perform(put("/workspaces/{workspaceId}/workdays", 101L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(List.of())))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(workdayRepo).delete(existingWorkday);
    }

    @Test
    public void testUpdateWorkdaysByWorkspaceIdInternalServerError() throws Exception {
        when(workdayRepo.findByWorkspaceId(101L)).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(put("/workspaces/{workspaceId}/workdays", 101L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(List.of())))
                .andExpect(status().isInternalServerError());

        verify(workdayRepo, never()).save(any());
        verify(workdayRepo, never()).delete(any());
    }

    @Test
    public void testDeleteWorkday() throws Exception {
        doNothing().when(workdayRepo).deleteById(1L);

        mockMvc.perform(delete("/workdays/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteWorkdayNotFound() throws Exception {
        doThrow(new RuntimeException("Workday not found")).when(workdayRepo).deleteById(1L);

        mockMvc.perform(delete("/workdays/{id}", 1))
                .andExpect(status().isInternalServerError());
    }
}
