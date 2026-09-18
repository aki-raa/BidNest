package com.BidNest.BiddingService.repo;

import com.BidNest.BiddingService.model.ProxyBid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProxyBidRepo extends JpaRepository<ProxyBid, UUID> {

    Optional<ProxyBid> findByAuctionIdAndBidderIdAndActiveTrue(UUID auctionId, UUID bidderId);

    List<ProxyBid> findByBidderIdAndActiveTrue(UUID bidderId);

    List<ProxyBid> findByAuctionIdAndActiveTrueOrderByMaxAmountDesc(UUID auctionId);
}