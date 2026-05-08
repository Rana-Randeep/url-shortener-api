package com.urlshortener.url_shortener_api.dto.request;

public class ShortenRequest {

    private String originalUrl;
    private String expiresAt;  // optional, user can send expiry date as string

    public ShortenRequest() {}

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getExpiresAt() {
        return expiresAt;
    }


}
