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
public class ProxyBidResponse {
    private UUID id;
    private UUID auctionId;
    private UUID bidderId;
    private BigDecimal maxAmount;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}