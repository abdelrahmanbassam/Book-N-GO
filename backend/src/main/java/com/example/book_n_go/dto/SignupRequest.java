package com.example.book_n_go.dto;

import com.example.book_n_go.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    private String email;
    private String password;
    private String name;
    private String phone;
    private Role role;
}
