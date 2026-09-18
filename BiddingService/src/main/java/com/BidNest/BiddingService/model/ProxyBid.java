package com.BidNest.BiddingService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(
        name = "proxy_bid_details",
        indexes = {
                @Index(name = "idx_auction_bidder", columnList = "auctionId, bidderId")
        }
)
@NoArgsConstructor
@AllArgsConstructor
public class ProxyBid {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID auctionId;

    @Column(nullable = false)
    private UUID bidderId;

    @Column(nullable = false)
    private BigDecimal maxAmount;

    private boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        active = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}