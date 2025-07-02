package com.example.RateLimitedURLShortener.DTO;

public class ShortenResponse {

    private String code;
    private String shortUrl;

    public String getCode() {
        return code;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public ShortenResponse(String code, String shortUrl) {
        this.code = code;
        this.shortUrl = shortUrl;
    }
}
