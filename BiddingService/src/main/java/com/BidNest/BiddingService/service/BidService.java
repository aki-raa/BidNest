package com.BidNest.BiddingService.service;

import com.BidNest.BiddingService.client.ListingClient;
import com.BidNest.BiddingService.dto.*;
import com.BidNest.BiddingService.kafka.BidEventProducer;
import com.BidNest.BiddingService.model.BidModel;
import com.BidNest.BiddingService.model.ProxyBid;
import com.BidNest.BiddingService.repo.BidRepo;
import com.BidNest.BiddingService.repo.ProxyBidRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BidService {

    @Autowired
    private BidEventProducer bidEventProducer;

    @Autowired
    private BidRepo bidRepo;

    @Autowired
    private ProxyBidRepo proxyBidRepo;

    @Autowired
    private ListingClient listingClient;

    public BidResponse placeBid(CreateBidRequest request, String bidderId, String token) {

        AuctionInfo auction = listingClient.getAuction(request.getAuctionId(), token);

        if (auction == null) {
            throw new RuntimeException("Auction not found");
        }
        if (!"ACTIVE".equals(auction.getStatus())) {
            throw new IllegalStateException("Auction is not active");
        }
        if (LocalDateTime.now().isAfter(auction.getEndTime())) {
            throw new IllegalStateException("Auction has already ended");
        }

        // Use Bidding Service's OWN database as source of truth for current highest bid,
        // since Listings Service's currentHighestBid is only updated AFTER a Kafka event,
        // so it can briefly be stale compared to what's actually in Bidding Service's DB.
        BidModel currentHighestInDb = bidRepo.findTopByAuctionIdOrderByAmountDesc(request.getAuctionId());
        BigDecimal actualCurrentHighest = (currentHighestInDb != null)
                ? currentHighestInDb.getAmount()
                : auction.getStartingPrice();

        BigDecimal minRequired = actualCurrentHighest.add(auction.getMinIncrement());
        if (request.getAmount().compareTo(minRequired) < 0) {
            throw new IllegalArgumentException("Bid must be at least " + minRequired);
        }

        UUID previousHighestBidderId = (currentHighestInDb != null)
                ? currentHighestInDb.getBidderId()
                : null;

        BidModel bid = new BidModel();
        bid.setAuctionId(request.getAuctionId());
        bid.setBidderId(UUID.fromString(bidderId));
        bid.setAmount(request.getAmount());
        bid.setAutoBid(false);

        BidModel saved = bidRepo.save(bid);

        // publish Kafka event — skip notifying if bidder is outbidding themselves
        if (previousHighestBidderId != null && !previousHighestBidderId.equals(saved.getBidderId())) {
            BidPlacedEvent event = new BidPlacedEvent(
                    saved.getAuctionId(),
                    saved.getBidderId(),
                    previousHighestBidderId,
                    saved.getAmount(),
                    saved.getPlacedAt()
            );
            bidEventProducer.publishBidPlaced(event);
        }

        return mapToBidResponse(saved);
    }

    public List<BidResponse> getBidsForAuction(UUID auctionId) {
        return bidRepo.findByAuctionIdOrderByAmountDesc(auctionId)
                .stream().map(this::mapToBidResponse).toList();
    }

    public List<BidResponse> getMyBids(String bidderId) {
        return bidRepo.findByBidderIdOrderByPlacedAtDesc(UUID.fromString(bidderId))
                .stream().map(this::mapToBidResponse).toList();
    }

    public BidResponse getHighestBid(UUID auctionId) {
        BidModel highest = bidRepo.findTopByAuctionIdOrderByAmountDesc(auctionId);
        if (highest == null) {
            throw new RuntimeException("No bids yet for this auction");
        }
        return mapToBidResponse(highest);
    }

    public ProxyBidResponse setProxyBid(CreateProxyBidRequest request, String bidderId) {
        UUID bidderUuid = UUID.fromString(bidderId);

        Optional<ProxyBid> existing = proxyBidRepo
                .findByAuctionIdAndBidderIdAndActiveTrue(request.getAuctionId(), bidderUuid);

        ProxyBid proxyBid = existing.orElseGet(ProxyBid::new);
        proxyBid.setAuctionId(request.getAuctionId());
        proxyBid.setBidderId(bidderUuid);
        proxyBid.setMaxAmount(request.getMaxAmount());
        proxyBid.setActive(true);

        ProxyBid saved = proxyBidRepo.save(proxyBid);

        // TODO later: trigger proxy-bid engine to immediately compete if needed

        return mapToProxyBidResponse(saved);
    }

    public void cancelProxyBid(UUID auctionId, String bidderId) {
        ProxyBid proxyBid = proxyBidRepo
                .findByAuctionIdAndBidderIdAndActiveTrue(auctionId, UUID.fromString(bidderId))
                .orElseThrow(() -> new RuntimeException("No active proxy bid found"));

        if (!proxyBid.getBidderId().equals(UUID.fromString(bidderId))) {
            throw new AccessDeniedException("You do not own this proxy bid");
        }

        proxyBid.setActive(false);
        proxyBidRepo.save(proxyBid);
    }

    public List<ProxyBidResponse> getMyProxyBids(String bidderId) {
        return proxyBidRepo.findByBidderIdAndActiveTrue(UUID.fromString(bidderId))
                .stream().map(this::mapToProxyBidResponse).toList();
    }

    private BidResponse mapToBidResponse(BidModel bid) {
        return new BidResponse(
                bid.getId(),
                bid.getAuctionId(),
                bid.getBidderId(),
                bid.getAmount(),
                bid.isAutoBid(),
                bid.getPlacedAt()
        );
    }

    private ProxyBidResponse mapToProxyBidResponse(ProxyBid proxyBid) {
        return new ProxyBidResponse(
                proxyBid.getId(),
                proxyBid.getAuctionId(),
                proxyBid.getBidderId(),
                proxyBid.getMaxAmount(),
                proxyBid.isActive(),
                proxyBid.getCreatedAt(),
                proxyBid.getUpdatedAt()
        );
    }
}