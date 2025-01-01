package com.example.book_n_go.controller;

import com.example.book_n_go.model.Aminity;
import com.example.book_n_go.repository.AminityRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class AminityControllerTest {

    @Mock
    private AminityRepo aminityRepo;

    @InjectMocks
    private AminityController aminityController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(aminityController).build();
    }

    @Test
    public void testGetAminities_EmptyList() throws Exception {
        when(aminityRepo.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/aminities"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetAminities_Success() throws Exception {
        Aminity aminity = new Aminity();
        aminity.setId(1L);
        aminity.setName("WiFi");

        List<Aminity> aminities = List.of(aminity);
        when(aminityRepo.findAll()).thenReturn(aminities);

        mockMvc.perform(get("/aminities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("WiFi"));
    }

    @Test
    public void testGetAminities_Exception() throws Exception {
        when(aminityRepo.findAll()).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/aminities"))
                .andExpect(status().isInternalServerError());
    }
}
