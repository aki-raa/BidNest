package com.BidNest.AuthService.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class Response {

    private String username;
//    private String password;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

