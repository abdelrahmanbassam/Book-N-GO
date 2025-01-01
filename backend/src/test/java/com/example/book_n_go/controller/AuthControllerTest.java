package com.example.book_n_go.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.book_n_go.dto.AuthResponse;
import com.example.book_n_go.dto.LoginRequest;
import com.example.book_n_go.dto.SignupRequest;
import com.example.book_n_go.enums.Role;
import com.example.book_n_go.model.User;
import com.example.book_n_go.repository.UserRepo;
import com.example.book_n_go.service.AuthService;
import com.example.book_n_go.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthControllerTest {

        private MockMvc mockMvc;

        @Mock
        private AuthService authService;

        @Mock
        private JwtService jwtService;

        @Mock
        private UserRepo userRepo;

        @Mock
        private PasswordEncoder passwordEncoder;

        @InjectMocks
        private AuthController authController;

        @BeforeEach
        public void setUp() {
                MockitoAnnotations.openMocks(this);
                mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        }

        @Test
        public void testSignup() throws Exception {
                SignupRequest signupRequest = SignupRequest.builder()
                                .email("test@example.com")
                                .password("password")
                                .name("Test User")
                                .phone("1234567890")
                                .role(Role.CLIENT)
                                .build();

                AuthResponse authResponse = new AuthResponse("dummyToken");

                when(authService.signup(any(SignupRequest.class))).thenReturn(authResponse);

                mockMvc.perform(post("/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(signupRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token", is("dummyToken")));
        }

        @Test
        public void testLogin() throws Exception {
                LoginRequest loginRequest = LoginRequest.builder()
                                .email("test@example.com")
                                .password("password")
                                .build();

                AuthResponse authResponse = new AuthResponse("dummyToken");

                when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

                mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(loginRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token", is("dummyToken")));
        }

        @Test
        public void testLoginUnauthorized() throws Exception {
                LoginRequest loginRequest = LoginRequest.builder()
                                .email("test@example.com")
                                .password("password")
                                .build();

                when(authService.login(any(LoginRequest.class))).thenThrow(new RuntimeException("Unauthorized"));

                mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(loginRequest)))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        public void testGetUserByToken() throws Exception {
                User user = new User();
                user.setEmail("test@example.com");
                user.setName("Test User");
                user.setPhone("1234567890");
                user.setRole(Role.CLIENT);

                // Mock SecurityContext and Authentication
                Authentication authentication = mock(Authentication.class);
                when(authentication.getPrincipal()).thenReturn(user);
                SecurityContext securityContext = mock(SecurityContext.class);
                when(securityContext.getAuthentication()).thenReturn(authentication);
                SecurityContextHolder.setContext(securityContext);

                when(authService.getRequestUser()).thenReturn(user);

                mockMvc.perform(get("/auth/get-user-info"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.email", is("test@example.com")))
                                .andExpect(jsonPath("$.name", is("Test User")))
                                .andExpect(jsonPath("$.phone", is("1234567890")))
                                .andExpect(jsonPath("$.role", is("CLIENT")));
        }

        @Test
        public void testUpdateRole() throws Exception {
            String token = "Bearer dummyToken";
            String jwt = "dummyToken";
            String email = "test@example.com";
        
            User user = new User();
            user.setEmail(email);
            user.setName("Test User");
            user.setPhone("1234567890");
            user.setRole(Role.CLIENT);
        
            when(jwtService.extractEmail(jwt)).thenReturn(email);
            when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
            when(passwordEncoder.encode("newpassword")).thenReturn("encodedNewPassword");
        
            mockMvc.perform(post("/auth/update-role")
                            .header("Authorization", token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"role\":\"ADMIN\", \"phone\":\"0987654321\", \"password\":\"newpassword\"}"))
                    .andExpect(status().isOk());
        
            verify(userRepo).save(any(User.class));
        }

        @Test
        public void testUpdateRoleUserNotFound() throws Exception {
                String token = "Bearer dummyToken";
                String jwt = "dummyToken";
                String email = "test@example.com";

                when(jwtService.extractEmail(jwt)).thenReturn(email);
                when(userRepo.findByEmail(email)).thenReturn(Optional.empty());

                mockMvc.perform(post("/auth/update-role")
                                .header("Authorization", token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"role\":\"ADMIN\", \"phone\":\"0987654321\", \"password\":\"newpassword\"}"))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string("Failed to update role"));
        }
}