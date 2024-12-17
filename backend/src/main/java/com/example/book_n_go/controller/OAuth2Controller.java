package com.example.book_n_go.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.book_n_go.dto.AuthResponse;
import com.example.book_n_go.enums.Role;
import com.example.book_n_go.model.User;
import com.example.book_n_go.repository.UserRepo;
import com.example.book_n_go.service.JwtService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final JwtService jwtService;
    private final UserRepo userRepo;

    @GetMapping("/oauth2-success")
    public AuthResponse oauth2Success(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String password = oAuth2User.getAttribute("sub");
        
        User user;
        try {
            user = userRepo.findByEmail(email).get();
        } catch (Exception e) {
            user = User.builder()
                    .email(email)
                    .password(password)
                    .name(name)
                    .phone("")
                    .role(Role.CLIENT)
                    .build();
        }

        userRepo.save(user);
        var jwt = jwtService.generateToken(user);
        return AuthResponse.builder().token(jwt).build();
    }
}
