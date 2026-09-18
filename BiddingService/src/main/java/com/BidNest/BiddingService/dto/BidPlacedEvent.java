package com.BidNest.BiddingService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidPlacedEvent {
    private UUID auctionId;
    private UUID bidderId;
    private UUID previousHighestBidderId; // null if this is the auction's first bid
    private BigDecimal amount;
    private LocalDateTime placedAt;
}