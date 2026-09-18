package com.BidNest.AuthService.controller;

import com.BidNest.AuthService.dto.Request;
import com.BidNest.AuthService.jwt.JwtUtil;
import com.BidNest.AuthService.model.UserModel;
import com.BidNest.AuthService.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<Request> request(@RequestBody Request request) {
        Request saved = authService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<String> userDtoResponseEntity(@RequestBody Request request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserModel user = authService.loginUser(request);
        String token = jwtUtil.generateToken(user.getId().toString(), user.getRole().name());
        return ResponseEntity.ok(token);
    }
}