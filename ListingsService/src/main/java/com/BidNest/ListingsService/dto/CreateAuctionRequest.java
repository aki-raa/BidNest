package com.BidNest.ListingsService.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateAuctionRequest {
    private String title;
    private String description;
    private BigDecimal startingPrice;
    private BigDecimal minIncrement;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    // no sellerId, no status, no id, no currentHighestBid
    // sellerId comes from JWT, status defaults to DRAFT, currentHighestBid defaults to startingPrice
}