package com.BidNest.BiddingService;


import java.util.concurrent.ConcurrentHashMap;

public class Store {
    private final ConcurrentHashMap<String, String> data = new ConcurrentHashMap<>();

    public void put(String key, String value) {
        data.put(key, value);
    }

    public String get(String key) {
        return data.get(key); // returns null if not found
    }

    public boolean delete(String key) {
        return data.remove(key) != null;
    }
}