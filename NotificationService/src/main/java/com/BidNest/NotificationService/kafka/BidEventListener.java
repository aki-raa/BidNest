package com.BidNest.NotificationService.kafka;

import com.BidNest.NotificationService.NotificationType;
import com.BidNest.NotificationService.dto.BidPlacedEvent;
import com.BidNest.NotificationService.model.NotificationLog;
import com.BidNest.NotificationService.repo.NotificationRepo;
import com.BidNest.NotificationService.websocket.NotificationPusher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BidEventListener {

    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private NotificationPusher notificationPusher;

    @KafkaListener(topics = "bid-placed", groupId = "notification-service-group")
    public void handleBidPlaced(BidPlacedEvent event) {

        // Notify the previous highest bidder that they've been outbid
        if (event.getPreviousHighestBidderId() != null) {
            NotificationLog log = new NotificationLog();
            log.setUserId(event.getPreviousHighestBidderId());
            log.setAuctionId(event.getAuctionId());
            log.setType(NotificationType.OUTBID);
            log.setMessage("You've been outbid! New highest bid: " + event.getAmount());

            NotificationLog saved = notificationRepo.save(log);
            saved.setDelivered(true);
            notificationRepo.save(saved);

            notificationPusher.pushToUser(
                    event.getPreviousHighestBidderId().toString(),
                    saved.getMessage()
            );
        }
    }
}