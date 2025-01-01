package com.example.book_n_go.controller;

import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.book_n_go.dto.AuthResponse;
import com.example.book_n_go.dto.LoginRequest;
import com.example.book_n_go.dto.SignupRequest;
import com.example.book_n_go.enums.Role;
import com.example.book_n_go.model.User;
import com.example.book_n_go.repository.UserRepo;
import com.example.book_n_go.service.AuthService;
import com.example.book_n_go.service.JwtService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private final AuthService authService;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final UserRepo userRepo;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/get-user-info")
    public ResponseEntity<User> getUserByToken() {
        return ResponseEntity.ok(AuthService.getRequestUser());
    }

    @PostMapping("/update-role")
    public ResponseEntity<?> updateRole(@RequestBody Map<String, String> request, @RequestHeader("Authorization") String token) {
        try {
            String role = request.get("role");
            String phone = request.get("phone");
            String password = request.get("password");
            String jwt = token.substring(7);
            String email = jwtService.extractEmail(jwt);
    
            User user = userRepo.findByEmail(email).orElseThrow(() -> new NoSuchElementException("User not found"));
            user.setRole(Role.valueOf(role));
            user.setPhone(phone);
            String encryptedPassword = passwordEncoder.encode(password);
            user.setPassword(encryptedPassword);
            userRepo.save(user);
    
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update role");
        }
    }

}
