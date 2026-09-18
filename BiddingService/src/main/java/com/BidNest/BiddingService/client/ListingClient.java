package com.BidNest.BiddingService.client;

import com.BidNest.BiddingService.dto.AuctionInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class ListingClient {

    @Value("${listings.service.url}")
    private String listingsServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public AuctionInfo getAuction(UUID auctionId, String token) {
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        org.springframework.http.HttpEntity<Void> entity = new org.springframework.http.HttpEntity<>(headers);

        org.springframework.http.ResponseEntity<AuctionInfo> response = restTemplate.exchange(
                listingsServiceUrl + "/api/auctions/" + auctionId,
                org.springframework.http.HttpMethod.GET,
                entity,
                AuctionInfo.class
        );

        return response.getBody();
    }
}