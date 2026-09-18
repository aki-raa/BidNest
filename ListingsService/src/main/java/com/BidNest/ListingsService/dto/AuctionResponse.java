package com.BidNest.ListingsService.dto;

import com.BidNest.ListingsService.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionResponse {
    private UUID id;
    private UUID sellerId;
    private String title;
    private String description;
    private BigDecimal startingPrice;
    private BigDecimal minIncrement;
    private BigDecimal currentHighestBid;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // this one IS safe to expose everything, since it's server → client only
}