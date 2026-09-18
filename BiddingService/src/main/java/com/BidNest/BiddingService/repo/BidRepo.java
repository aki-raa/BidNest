package com.BidNest.BiddingService.repo;

import com.BidNest.BiddingService.model.BidModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BidRepo extends JpaRepository<BidModel, UUID> {

    List<BidModel> findByAuctionIdOrderByAmountDesc(UUID auctionId);

    List<BidModel> findByBidderIdOrderByPlacedAtDesc(UUID bidderId);

    BidModel findTopByAuctionIdOrderByAmountDesc(UUID auctionId);
}