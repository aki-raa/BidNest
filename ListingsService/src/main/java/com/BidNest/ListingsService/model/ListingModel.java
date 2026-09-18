package com.BidNest.ListingsService.model;

import com.BidNest.ListingsService.Status;
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
        name = "listing_details",
        indexes = {
                @Index(name = "idx_status_endtime", columnList = "status, endTime")
        }
)
@NoArgsConstructor
@AllArgsConstructor
public class ListingModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID sellerId;

    private String title;
    private String description;

    private BigDecimal startingPrice;
    private BigDecimal minIncrement;
    private BigDecimal currentHighestBid;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (currentHighestBid == null) {
            currentHighestBid = startingPrice;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}