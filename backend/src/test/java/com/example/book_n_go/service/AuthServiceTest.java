package com.example.book_n_go.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.book_n_go.dto.AuthResponse;
import com.example.book_n_go.dto.LoginRequest;
import com.example.book_n_go.dto.SignupRequest;
import com.example.book_n_go.enums.Role;
import com.example.book_n_go.model.User;
import com.example.book_n_go.repository.UserRepo;

public class AuthServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignup() {
        SignupRequest signupRequest = SignupRequest.builder()
                .email("test@example.com")
                .password("password")
                .name("Test User")
                .phone("1234567890")
                .role(Role.CLIENT)
                .build();

        User user = User.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .name("Test User")
                .phone("1234567890")
                .role(Role.CLIENT)
                .build();

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepo.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("dummyToken");

        AuthResponse response = authService.signup(signupRequest);

        assertEquals("dummyToken", response.getToken());
    }

    @Test
    public void testLogin() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();

        User user = User.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .name("Test User")
                .phone("1234567890")
                .role(Role.CLIENT)
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("dummyToken");

        AuthResponse response = authService.login(loginRequest);

        assertEquals("dummyToken", response.getToken());
    }
}