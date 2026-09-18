package com.BidNest.BiddingService.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateProxyBidRequest {
    private UUID auctionId;
    private BigDecimal maxAmount;
    // no bidderId — comes from JWT
}