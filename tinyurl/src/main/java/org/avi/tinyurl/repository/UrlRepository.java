package org.avi.tinyurl.repository;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

@Repository
public class UrlRepository {
    private final ConcurrentHashMap<String, String> store = new ConcurrentHashMap<>();

    public void save(String shortId, String longUrl) {
        store.put(shortId, longUrl);
    }

    public String findLongUrl(String shortId) {
        return store.get(shortId);
    }

    public boolean exists(String shortId) {
        return store.containsKey(shortId);
    }
}

