package com.BidNest.ListingsService.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UpdateAuctionRequest {
    private String title;
    private String description;
    private BigDecimal startingPrice;
    private BigDecimal minIncrement;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    // still no sellerId, status, or currentHighestBid here either
    // status changes go through a separate endpoint (PATCH /auctions/{id}/status)
}