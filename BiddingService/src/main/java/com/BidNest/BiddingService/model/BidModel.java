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
        name = "bid_details",
        indexes = {
                @Index(name = "idx_auction_amount", columnList = "auctionId, amount")
        }
)
@NoArgsConstructor
@AllArgsConstructor
public class BidModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID auctionId;

    @Column(nullable = false)
    private UUID bidderId;

    @Column(nullable = false)
    private BigDecimal amount;

    private boolean isAutoBid;

    private LocalDateTime placedAt;

    @Version
    private Long version; // optimistic locking

    @PrePersist
    protected void onCreate() {
        placedAt = LocalDateTime.now();
    }
}