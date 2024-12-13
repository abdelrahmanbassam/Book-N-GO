package com.example.book_n_go.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.book_n_go.enums.Permission;
import com.example.book_n_go.enums.Role;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/auth/signup", "/auth/login")
                    .permitAll()

                .requestMatchers("/admin/**").hasRole(Role.ADMIN.name())
                    .requestMatchers(HttpMethod.GET, "/admin/**").hasAuthority(Permission.ADMIN_READ.name())
                    .requestMatchers(HttpMethod.POST, "/admin/**").hasAuthority(Permission.ADMIN_WRITE.name())

                .requestMatchers("/provider/**").hasAnyRole(Role.ADMIN.name(), Role.PROVIDER.name())
                    .requestMatchers(HttpMethod.GET, "/provider/**").hasAnyAuthority(Permission.ADMIN_READ.name(), Permission.PROVIDER_READ.name())
                    .requestMatchers(HttpMethod.POST, "/provider/**").hasAnyAuthority(Permission.ADMIN_WRITE.name(), Permission.PROVIDER_WRITE.name())

                .requestMatchers("/client/**").hasAnyRole(Role.ADMIN.name(), Role.CLIENT.name())
                    .requestMatchers(HttpMethod.GET, "/client/**").hasAnyAuthority(Permission.ADMIN_READ.name(), Permission.CLIENT_READ.name())
                    .requestMatchers(HttpMethod.POST, "/client/**").hasAnyAuthority(Permission.ADMIN_WRITE.name(), Permission.CLIENT_WRITE.name())
                    
                .anyRequest()
                    .authenticated()
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
