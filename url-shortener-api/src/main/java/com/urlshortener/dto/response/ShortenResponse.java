package com.urlshortener.dto.response;

public class ShortenResponse {

    private String shortUrl;
    private String originalUrl;
    private String expiresAt;
    private String qrCodeUrl;

    public ShortenResponse() {};

    public ShortenResponse(String shortUrl, String originalUrl,
                           String expiresAt, String qrCodeUrl) {
        this.shortUrl = shortUrl;
        this.originalUrl = originalUrl;
        this.expiresAt = expiresAt;
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }
}
