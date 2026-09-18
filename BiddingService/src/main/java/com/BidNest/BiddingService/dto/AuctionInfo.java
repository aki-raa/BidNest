package com.BidNest.BiddingService.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AuctionInfo {
    private UUID id;
    private UUID sellerId;
    private String title;
    private BigDecimal startingPrice;
    private BigDecimal minIncrement;
    private BigDecimal currentHighestBid;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    // used when Bidding Service calls Listings Service via RestTemplate/WebClient
    // to validate a bid before accepting it
}