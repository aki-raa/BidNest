package com.BidNest.ListingsService.controller;

import com.BidNest.ListingsService.dto.AuctionResponse;
import com.BidNest.ListingsService.dto.CreateAuctionRequest;
import com.BidNest.ListingsService.dto.UpdateAuctionRequest;
import com.BidNest.ListingsService.dto.UpdateStatusRequest;
import com.BidNest.ListingsService.service.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ListingController {

    @Autowired
    private ListingService listingService;

    @PostMapping("/auctions")
    public ResponseEntity<AuctionResponse> createAuction(
            @RequestBody CreateAuctionRequest request, Authentication authentication) {
        AuctionResponse response = listingService.createAnAuction(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/auctions")
    public ResponseEntity<List<AuctionResponse>> getAllAuctions(
            @RequestParam(required = false) String status, Authentication authentication) {
        return ResponseEntity.ok(listingService.listOfAllAuctions(status, authentication.getName()));
    }

    @GetMapping("/auctions/{id}")
    public ResponseEntity<AuctionResponse> getAuctionById(
            @PathVariable UUID id, Authentication authentication) {
        return ResponseEntity.ok(listingService.getAuctionById(id, authentication.getName()));
    }

    @GetMapping("/auctions/ending-soon")
    public ResponseEntity<List<AuctionResponse>> seeAuctionEndingTime(Authentication authentication) {
        return ResponseEntity.ok(listingService.seeAuctionEndingTime(authentication.getName()));
    }

    @PutMapping("/auctions/{id}")
    public ResponseEntity<AuctionResponse> updateAuction(
            @PathVariable UUID id, @RequestBody UpdateAuctionRequest request, Authentication authentication) {
        return ResponseEntity.ok(listingService.updateAuction(id, request, authentication.getName()));
    }

    @PatchMapping("/auctions/{id}/status")
    public ResponseEntity<AuctionResponse> updateAuctionStatus(
            @PathVariable UUID id, @RequestBody UpdateStatusRequest request, Authentication authentication) {
        return ResponseEntity.ok(listingService.sellAuctionById(request, id, authentication.getName()));
    }

    @GetMapping("/auctions/seller/{sellerId}")
    public ResponseEntity<List<AuctionResponse>> getAuctionsBySeller(
            @PathVariable UUID sellerId, Authentication authentication) {
        return ResponseEntity.ok(listingService.getAuctionsBySeller(sellerId, authentication.getName()));
    }
}