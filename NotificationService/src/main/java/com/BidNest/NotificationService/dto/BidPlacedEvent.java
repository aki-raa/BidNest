package com.BidNest.NotificationService.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BidPlacedEvent {
    private UUID auctionId;
    private UUID bidderId;
    private UUID previousHighestBidderId; // nullable if this is the first bid
    private BigDecimal amount;
    private LocalDateTime placedAt;
}