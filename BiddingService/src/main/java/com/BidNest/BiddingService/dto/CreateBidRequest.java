package com.BidNest.BiddingService.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateBidRequest {
    private UUID auctionId;
    private BigDecimal amount;
    // no bidderId — comes from JWT
    // no isAutoBid — server sets this to false for manual bids
}