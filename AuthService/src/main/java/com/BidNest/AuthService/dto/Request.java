package com.BidNest.AuthService.dto;

import com.BidNest.AuthService.Role;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Request {
    private UUID id;
    private String username;
    private String password;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Role role;
}
