package com.BidNest.AuthService.service;

import com.BidNest.AuthService.dto.Request;
import com.BidNest.AuthService.model.UserModel;
import com.BidNest.AuthService.repository.AuthRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    AuthRepository authRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    Request request;

    @BeforeEach
    void setUp() {
        request = new Request();
        request.setUsername("sunil");
        request.setEmail("sunil@gmail.com");
        request.setPassword("12345");
        // request.setRole(Role.USER); // uncomment/adjust to your actual Role enum
    }



    @Test
    void registerUserWhenEmailIsNotPutted(){

        /

    }


    @Test
    void registerUser_savesNewUser_whenEmailNotAlreadyRegistered() {
        // Arrange
        when(authRepository.findUserByEmail(request.getEmail()))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword()))
                .thenReturn("hashed-password");

        UserModel savedUser = new UserModel();
        savedUser.setId(UUID.randomUUID());
        savedUser.setUsername(request.getUsername());
        savedUser.setEmail(request.getEmail());
        savedUser.setRole(request.getRole());
        savedUser.setPassword("hashed-password");

        when(authRepository.save(any(UserModel.class)))
                .thenReturn(savedUser);

        // Act
        Request result = authService.registerUser(request);

        // Assert
        assertNotNull(result);
        assertEquals(savedUser.getId(), result.getId());
        assertEquals("sunil", result.getUsername());
        assertEquals("sunil@gmail.com", result.getEmail());

        verify(authRepository).findUserByEmail(request.getEmail());
        verify(passwordEncoder).encode("12345");
        verify(authRepository).save(any(UserModel.class));
    }

    @Test
    void registerUser_throwsBadRequest_whenEmailAlreadyRegistered() {
        // Arrange
        when(authRepository.findUserByEmail(request.getEmail()))
                .thenReturn(Optional.of(new UserModel()));

        // Act + Assert
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> authService.registerUser(request)
        );

        assertEquals(400, ex.getStatusCode().value());
        assertTrue(ex.getReason().contains("Email already registered"));

        verify(authRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(anyString());
    }
//
//    @Test
//    void loginUser_returnsRoleName_whenUserExists() {
//        UserModel existingUser = new UserModel();
//        existingUser.setEmail(request.getEmail());
//        existingUser.setRole(request.getRole()); // set an actual Role enum value
//
//        when(authRepository.findUserByEmail(request.getEmail()))
//                .thenReturn(Optional.of(existingUser));
//
//        String role = authService.loginUser(request);
//
//        assertNotNull(role);
//        verify(authRepository).findUserByEmail(request.getEmail());
//    }

    @Test
    void loginUser_throwsRuntimeException_whenUserNotFound() {
        when(authRepository.findUserByEmail(request.getEmail()))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> authService.loginUser(request)
        );

        assertEquals("User not found", ex.getMessage());
    }
}