package com.BidNest.BiddingService.controller;

import com.BidNest.BiddingService.dto.*;
import com.BidNest.BiddingService.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class BidController {

    @Autowired
    private BidService biddingService;

    @PostMapping("/bids")
    public ResponseEntity<BidResponse> placeBid(
            @RequestBody CreateBidRequest request,
            Authentication authentication,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        BidResponse response = biddingService.placeBid(request, authentication.getName(), token);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/bids/auction/{auctionId}")
    public ResponseEntity<List<BidResponse>> getBidsForAuction(@PathVariable UUID auctionId) {
        return ResponseEntity.ok(biddingService.getBidsForAuction(auctionId));
    }

    @GetMapping("/bids/my-bids")
    public ResponseEntity<List<BidResponse>> getMyBids(Authentication authentication) {
        return ResponseEntity.ok(biddingService.getMyBids(authentication.getName()));
    }

    @GetMapping("/bids/auction/{auctionId}/highest")
    public ResponseEntity<BidResponse> getHighestBid(@PathVariable UUID auctionId) {
        return ResponseEntity.ok(biddingService.getHighestBid(auctionId));
    }

    @PostMapping("/proxy-bids")
    public ResponseEntity<ProxyBidResponse> setProxyBid(
            @RequestBody CreateProxyBidRequest request, Authentication authentication) {
        ProxyBidResponse response = biddingService.setProxyBid(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/proxy-bids/{auctionId}")
    public ResponseEntity<Void> cancelProxyBid(
            @PathVariable UUID auctionId, Authentication authentication) {
        biddingService.cancelProxyBid(auctionId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/proxy-bids/my-proxy-bids")
    public ResponseEntity<List<ProxyBidResponse>> getMyProxyBids(Authentication authentication) {
        return ResponseEntity.ok(biddingService.getMyProxyBids(authentication.getName()));
    }
}