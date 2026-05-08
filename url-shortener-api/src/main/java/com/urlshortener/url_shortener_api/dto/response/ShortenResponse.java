package com.urlshortener.url_shortener_api.dto.response;

import com.urlshortener.url_shortener_api.dto.request.ShortenRequest;

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

    private String getShortUrl() {
        return shortUrl;
    }

    private void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    private String getOriginalUrl() {
        return originalUrl;
    }

    private void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }


}
