package com.example.book_n_go.dto;

import lombok.Data;

@Data
public class LoginRequest {
    // private String usernameOrEmail;
    private String email;
    private String password;
}
