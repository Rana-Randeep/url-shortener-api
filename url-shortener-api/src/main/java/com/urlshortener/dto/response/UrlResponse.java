package com.urlshortener.dto.response;

public class UrlResponse {

    private String shortCode;
    private String originalUrl;
    private String shortUrl;
    private String createdAt;
    private String expiresAt;

    public UrlResponse() {
    }

    public UrlResponse(String shortCode, String originalUrl, String shortUrl,
                       String createdAt, String expiresAt) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public String getShortCode() { return shortCode; }
    public String getOriginalUrl() { return originalUrl; }
    public String getShortUrl() { return shortUrl; }
    public String getCreatedAt() { return createdAt; }
    public String getExpiresAt() { return expiresAt; }

    public void setShortCode(String shortCode) { this.shortCode = shortCode; }
    public void setOriginalUrl(String originalUrl) { this.originalUrl = originalUrl; }
    public void setShortUrl(String shortUrl) { this.shortUrl = shortUrl; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setExpiresAt(String expiresAt) { this.expiresAt = expiresAt; }
}