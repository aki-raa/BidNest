package com.BidNest.BiddingService.kafka;

import com.BidNest.BiddingService.dto.BidPlacedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class BidEventProducer {

    private static final String TOPIC = "bid-placed";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void publishBidPlaced(BidPlacedEvent event) {
        kafkaTemplate.send(TOPIC, event.getAuctionId().toString(), event);
    }
}