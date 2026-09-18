package com.BidNest.AuthService.service;

import com.BidNest.AuthService.dto.Request;
import com.BidNest.AuthService.model.UserModel;
import com.BidNest.AuthService.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // register call
    public Request registerUser(Request request) {
        if (authRepository.findUserByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }
        UserModel userModel = new UserModel();
        userModel.setUsername(request.getUsername());
        userModel.setEmail(request.getEmail());
        userModel.setRole(request.getRole());
        userModel.setCreatedAt(LocalDateTime.now());
        userModel.setUpdatedAt(LocalDateTime.now());
        userModel.setPassword(passwordEncoder.encode(request.getPassword()));

        UserModel saved = authRepository.save(userModel);
        return mapToDto(saved);
    }

    // login call — returns the full UserModel now, not just role
    public UserModel loginUser(Request request) {
        return authRepository.findUserByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Request mapToDto(UserModel userModel) {
        Request request = new Request();
        request.setId(userModel.getId());
        request.setUsername(userModel.getUsername());
        request.setRole(userModel.getRole());
        request.setEmail(userModel.getEmail());
        request.setCreatedAt(userModel.getCreatedAt());
        request.setUpdatedAt(userModel.getUpdatedAt());
        return request;
    }
}