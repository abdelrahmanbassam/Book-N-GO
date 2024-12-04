package com.example.book_n_go.controller;

import com.example.book_n_go.dto.AuthResponse;
import com.example.book_n_go.dto.LoginRequest;
import com.example.book_n_go.dto.SignupRequest;
import com.example.book_n_go.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        authService.signup(request.getEmail(), request.getPassword(), request.getPhone(), request.getName(), request.getRole());
        return new ResponseEntity <> (HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.getEmail(), request.getPassword());
        return new ResponseEntity <> (new AuthResponse(token), HttpStatus.OK);
    }

}
