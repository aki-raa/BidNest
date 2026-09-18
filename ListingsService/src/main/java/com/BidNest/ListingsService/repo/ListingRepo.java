package com.BidNest.ListingsService.repo;

import com.BidNest.ListingsService.Status;
import com.BidNest.ListingsService.model.ListingModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ListingRepo extends JpaRepository<ListingModel, UUID> {
    Optional<ListingModel> findAuctionById(UUID id);

    List<ListingModel> findByStatus(Status status);

    List<ListingModel> findBySellerId(UUID sellerId);

    List<ListingModel> findByStatusAndEndTimeBetweenOrderByEndTimeAsc(
            Status status, LocalDateTime start, LocalDateTime end);
}
