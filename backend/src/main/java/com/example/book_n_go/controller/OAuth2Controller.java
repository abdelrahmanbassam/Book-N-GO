package com.example.book_n_go.controller;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.example.book_n_go.enums.Role;
import com.example.book_n_go.model.User;
import com.example.book_n_go.repository.UserRepo;
import com.example.book_n_go.service.JwtService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class OAuth2Controller {

    private final UserRepo userRepo;
    private final JwtService jwtService;

    @GetMapping("/oauth2-success")
    public RedirectView oauth2Success(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        try {
            Optional<User> userfound = userRepo.findByEmail(email);
            if (userfound.isPresent()) {
                User user = userfound.get();
                Role role = user.getRole();
                String token = jwtService.generateToken(user);
    
                String redirectUrl = "http://localhost:3000";
                if (role == Role.CLIENT) {
                    redirectUrl += "/hallsList";
                } else if (role == Role.PROVIDER) {
                    redirectUrl += "/hallOwner";
                } else if (role == Role.ADMIN) {
                    redirectUrl += "/admin";
                }
                redirectUrl += "?token=" + token;
                return new RedirectView(redirectUrl);
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setPhone("example phone");
            user.setRole(Role.CLIENT);
            user.setPassword("example password");
            userRepo.save(user);
            String token = jwtService.generateToken(user);
            return new RedirectView("http://localhost:3000/select-role?token=" + token);
        }
    }
}
