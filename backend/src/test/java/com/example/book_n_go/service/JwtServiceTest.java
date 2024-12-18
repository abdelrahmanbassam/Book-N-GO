package com.example.book_n_go.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService = new JwtService();
        jwtService.secret = "5050c0560c39af2eb257538e4b3634879b9447075d9f02d15545a0207f21451b";
        jwtService.expirationTime = 3600000; // 1 hour
    }

    @Test
    public void testGenerateToken() {
        UserDetails userDetails = User.builder()
                .username("test@example.com")
                .password("password")
                .roles("USER")
                .build();

        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
    }

    @Test
    public void testExtractEmail() {
        UserDetails userDetails = User.builder()
                .username("test@example.com")
                .password("password")
                .roles("USER")
                .build();

        String token = jwtService.generateToken(userDetails);
        String email = jwtService.extractEmail(token);
        assertEquals("test@example.com", email);
    }

    @Test
    public void testIsTokenValid() {
        UserDetails userDetails = User.builder()
                .username("test@example.com")
                .password("password")
                .roles("USER")
                .build();

        String token = jwtService.generateToken(userDetails);
        boolean isValid = jwtService.isTokenValid(token, userDetails);
        assertTrue(isValid);
    }

    @Test
    public void testIsTokenExpired() {
        Claims claims = Jwts.claims().setSubject("test@example.com");
        claims.setIssuedAt(new Date(System.currentTimeMillis() - 3600000)); // 1 hour ago
        claims.setExpiration(new Date(System.currentTimeMillis() - 1800000)); // 30 minutes ago
    
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtService.secret)), SignatureAlgorithm.HS256)
                .compact();
    
        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> {
            jwtService.isTokenExpired(token);
        });
    }
}