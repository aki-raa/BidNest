package com.BidNest.BiddingService.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        // ===== DEBUG BLOCK START =====
        System.out.println("=== DEBUG START ===");
        System.out.println("Token received: " + token);
        System.out.println("Token valid? " + jwtUtil.isTokenValid(token));
        // ===== DEBUG BLOCK END =====

        if (jwtUtil.isTokenValid(token)) {
            String userId = jwtUtil.extractUserId(token); // UUID as string
            String role = jwtUtil.extractRole(token);

            // ===== DEBUG BLOCK START =====
            System.out.println("Extracted userId: " + userId);
            System.out.println("Extracted role: " + role);
            System.out.println("Authority being set: ROLE_" + role);
            // ===== DEBUG BLOCK END =====

            var authentication = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + role))
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // ===== DEBUG BLOCK START =====
            System.out.println("SecurityContext set successfully");
            // ===== DEBUG BLOCK END =====
        }

        System.out.println("=== DEBUG END ===");

        filterChain.doFilter(request, response);
    }
}