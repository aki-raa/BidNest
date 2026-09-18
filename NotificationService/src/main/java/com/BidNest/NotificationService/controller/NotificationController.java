package com.BidNest.NotificationService.controller;

import com.BidNest.NotificationService.dto.NotificationResponse;
import com.BidNest.NotificationService.model.NotificationLog;
import com.BidNest.NotificationService.repo.NotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationRepo notificationRepo;

    @GetMapping("/my-notifications")
    public ResponseEntity<List<NotificationResponse>> getMyNotifications(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        List<NotificationLog> logs = notificationRepo.findByUserIdOrderByCreatedAtDesc(userId);

        List<NotificationResponse> response = logs.stream()
                .map(log -> new NotificationResponse(
                        log.getId(), log.getAuctionId(), log.getType(),
                        log.getMessage(), log.isRead(), log.getCreatedAt()))
                .toList();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable UUID id, Authentication authentication) {
        NotificationLog log = notificationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!log.getUserId().equals(UUID.fromString(authentication.getName()))) {
            throw new org.springframework.security.access.AccessDeniedException("Not your notification");
        }

        log.setRead(true);
        notificationRepo.save(log);
        return ResponseEntity.ok().build();
    }
}