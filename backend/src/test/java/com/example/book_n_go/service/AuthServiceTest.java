package com.example.book_n_go.service;

import com.example.book_n_go.model.User;
import com.example.book_n_go.enums.Role;
import com.example.book_n_go.repository.UserRepository;
import com.example.book_n_go.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignupSuccess() {
        String email = "test@example.com";
        String password = "password";
        String phone = "1234567890";
        String name = "Test User";
        Role role = Role.CLIENT;

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(jwtUtils.generateJwtToken(email, role)).thenReturn("dummyToken");

        String token = authService.signup(email, password, phone, name, role);

        assertEquals("dummyToken", token);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testSignupEmailAlreadyExists() {
        String email = "test@example.com";
        String password = "password";
        String phone = "1234567890";
        String name = "Test User";
        Role role = Role.CLIENT;

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class, () -> {
            authService.signup(email, password, phone, name, role);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testLoginSuccess() {
        String email = "test@example.com";
        String password = "password";
        Role role = Role.CLIENT;

        User user = new User();
        user.setEmail(email);
        user.setPassword("encodedPassword");
        user.setRole(role);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(true);
        when(jwtUtils.generateJwtToken(email, role)).thenReturn("dummyToken");

        String token = authService.login(email, password);

        assertEquals("dummyToken", token);
    }

    @Test
    public void testLoginUserNotFound() {
        String email = "test@example.com";
        String password = "password";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            authService.login(email, password);
        });
    }

    @Test
    public void testLoginInvalidPassword() {
        String email = "test@example.com";
        String password = "password";

        User user = new User();
        user.setEmail(email);
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> {
            authService.login(email, password);
        });
    }
}