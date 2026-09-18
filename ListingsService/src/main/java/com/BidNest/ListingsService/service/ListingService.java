package com.BidNest.ListingsService.service;

import com.BidNest.ListingsService.Status;
import com.BidNest.ListingsService.dto.AuctionResponse;
import com.BidNest.ListingsService.dto.CreateAuctionRequest;
import com.BidNest.ListingsService.dto.UpdateAuctionRequest;
import com.BidNest.ListingsService.dto.UpdateStatusRequest;
import com.BidNest.ListingsService.model.ListingModel;
import com.BidNest.ListingsService.repo.ListingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ListingService {

    @Autowired
    private ListingRepo listingRepo;

    public AuctionResponse createAnAuction(CreateAuctionRequest request, String userId) {
        ListingModel auction = new ListingModel();
        auction.setSellerId(UUID.fromString(userId));
        auction.setTitle(request.getTitle());
        auction.setDescription(request.getDescription());
        auction.setStartingPrice(request.getStartingPrice());
        auction.setMinIncrement(request.getMinIncrement());
        auction.setCurrentHighestBid(request.getStartingPrice());
        auction.setStartTime(request.getStartTime());
        auction.setEndTime(request.getEndTime());
        auction.setStatus(Status.DRAFT);

        ListingModel savedAuction = listingRepo.save(auction);
        return mapToResponse(savedAuction);
    }

    public List<AuctionResponse> listOfAllAuctions(String status, String userId) {
        List<ListingModel> auctions;

        if (status != null && !status.isBlank()) {
            Status statusEnum = Status.valueOf(status.toUpperCase());
            auctions = listingRepo.findByStatus(statusEnum);
        } else {
            auctions = listingRepo.findAll();
        }

        return auctions.stream().map(this::mapToResponse).toList();
    }

    public AuctionResponse getAuctionById(UUID id, String userId) {
        ListingModel auction = listingRepo.findAuctionById(id)
                .orElseThrow(() -> new RuntimeException("Auction not found"));
        return mapToResponse(auction);
    }

    public List<AuctionResponse> seeAuctionEndingTime(String userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime soon = now.plusHours(24);

        List<ListingModel> endingSoon = listingRepo
                .findByStatusAndEndTimeBetweenOrderByEndTimeAsc(Status.ACTIVE, now, soon);

        return endingSoon.stream().map(this::mapToResponse).toList();
    }

    public AuctionResponse updateAuction(UUID id, UpdateAuctionRequest request, String userId) {
        ListingModel auction = listingRepo.findAuctionById(id)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        if (!auction.getSellerId().equals(UUID.fromString(userId))) {
            throw new AccessDeniedException("You do not own this auction");
        }

        if (auction.getStatus() != Status.DRAFT && auction.getStatus() != Status.ACTIVE) {
            throw new IllegalStateException("Cannot update a closed or cancelled auction");
        }

        auction.setTitle(request.getTitle());
        auction.setDescription(request.getDescription());
        auction.setStartingPrice(request.getStartingPrice());
        auction.setMinIncrement(request.getMinIncrement());
        auction.setStartTime(request.getStartTime());
        auction.setEndTime(request.getEndTime());
        // status, sellerId, currentHighestBid untouched here

        ListingModel updated = listingRepo.save(auction);
        return mapToResponse(updated);
    }

    public AuctionResponse sellAuctionById(UpdateStatusRequest request, UUID id, String userId) {
        ListingModel auction = listingRepo.findAuctionById(id)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        boolean isOwner = auction.getSellerId().equals(UUID.fromString(userId));
        if (!isOwner) {
            throw new AccessDeniedException("You do not own this auction");
        }

        Status newStatus = request.getStatus();
        Status currentStatus = auction.getStatus();

        boolean validTransition =
                (currentStatus == Status.DRAFT && newStatus == Status.ACTIVE) ||
                        (currentStatus == Status.ACTIVE && (newStatus == Status.CLOSED || newStatus == Status.CANCELLED)) ||
                        (currentStatus == Status.DRAFT && newStatus == Status.CANCELLED);

        if (!validTransition) {
            throw new IllegalStateException("Invalid status transition: " + currentStatus + " -> " + newStatus);
        }

        auction.setStatus(newStatus);
        ListingModel updated = listingRepo.save(auction);
        return mapToResponse(updated);
    }

    public List<AuctionResponse> getAuctionsBySeller(UUID sellerId, String userId) {
        List<ListingModel> auctions = listingRepo.findBySellerId(sellerId);
        return auctions.stream().map(this::mapToResponse).toList();
    }

    private AuctionResponse mapToResponse(ListingModel auction) {
        return new AuctionResponse(
                auction.getId(),
                auction.getSellerId(),
                auction.getTitle(),
                auction.getDescription(),
                auction.getStartingPrice(),
                auction.getMinIncrement(),
                auction.getCurrentHighestBid(),
                auction.getStartTime(),
                auction.getEndTime(),
                auction.getStatus(),
                auction.getCreatedAt(),
                auction.getUpdatedAt()
        );
    }
}