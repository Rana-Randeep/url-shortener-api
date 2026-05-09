package com.urlshortener.dto.response;

public class ShortenResponse {

    private String shortUrl;
    private String originalUrl;
    private String expiresAt;

    public ShortenResponse() {};

    public ShortenResponse(String shortUrl, String originalUrl, String expiresAt) {
        this.shortUrl = shortUrl;
        this.originalUrl = originalUrl;
        this.expiresAt = expiresAt;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }


}
