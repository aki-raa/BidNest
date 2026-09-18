package com.BidNest.BiddingService.model;

import com.BidNest.BiddingService.dto.AuctionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class ListingClient {

    @Value("${listings.service.url}")
    private String listingsServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    public AuctionInfo getAuction(UUID auctionId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<AuctionInfo> response = restTemplate.exchange(
                listingsServiceUrl + "/api/auctions/" + auctionId,
                HttpMethod.GET,
                entity,
                AuctionInfo.class
        );

        return response.getBody();
    }
}
