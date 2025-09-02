package org.avi.tinyurl.service;

import org.avi.tinyurl.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.security.SecureRandom;
import java.util.UUID;

@Service
public class UrlShortenerService {

    @Autowired
    private UrlRepository repository;

    private static final String BASE_URL = "http://localhost:8080/";

    public String shortenUrl(String longUrl) {
        try {
            new URL(longUrl).toURI(); // Basic validation
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid URL");
        }
        String shortId = generateUniqueId();
        repository.save(shortId, longUrl);
        return BASE_URL + shortId;
    }

    public String getOriginalUrl(String shortId) {
        return repository.findLongUrl(shortId);
    }

    private String generateUniqueId() {
        String id;
        do {
            id = UUID.randomUUID().toString().substring(0, 6);
        } while (repository.exists(id));
        return id;
    }
    private String generateSecureId() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(8); // or more
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

}

