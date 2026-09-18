package com.BidNest.NotificationService.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationPusher {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void pushToUser(String userId, String message) {
        messagingTemplate.convertAndSendToUser(
                userId,
                "/queue/notifications",
                message
        );
    }
}