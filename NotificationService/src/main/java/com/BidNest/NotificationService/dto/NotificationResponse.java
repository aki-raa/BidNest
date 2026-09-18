package com.BidNest.NotificationService.dto;

import com.BidNest.NotificationService.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private UUID id;
    private UUID auctionId;
    private NotificationType type;
    private String message;
    private boolean read;
    private LocalDateTime createdAt;
}