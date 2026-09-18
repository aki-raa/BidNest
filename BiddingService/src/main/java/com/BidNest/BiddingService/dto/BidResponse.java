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
public class BidResponse {
    private UUID id;
    private UUID auctionId;
    private UUID bidderId;
    private BigDecimal amount;
    private boolean isAutoBid;
    private LocalDateTime placedAt;
}