//package com.example.book_n_go.security;

//import com.example.book_n_go.security.JwtUtils;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import com.example.book_n_go.service.CustomUserDetailsService;
//
//import java.io.IOException;

//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//public class JwtAuthenticationFilter {

//    private final JwtUtils jwtUtils;
//    private final CustomUserDetailsService userDetailsService;
//
//    public JwtAuthenticationFilter(JwtUtils jwtUtils, CustomUserDetailsService userDetailsService) {
//        this.jwtUtils = jwtUtils;
//        this.userDetailsService = userDetailsService;
//    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
        // String authHeader = request.getHeader("Authorization");
        // String jwt = null;
        // String username = null;

        // // Extract JWT token and validate
        // if (authHeader != null && authHeader.startsWith("Bearer ")) {
        //     jwt = authHeader.substring(7);
        //     username = jwtUtils.extractClaims(jwt).getSubject();
        // }

        // if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        //     var userDetails = userDetailsService.loadUserByUsername(username);
        //     if (jwtUtils.isTokenValid(jwt, userDetails.getUsername())) {
        //         var authenticationToken = new UsernamePasswordAuthenticationToken(
        //                 userDetails, null, userDetails.getAuthorities());
        //         authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        //         SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //     }
        // }

        // filterChain.doFilter(request, response);
//    }
//}
