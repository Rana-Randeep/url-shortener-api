package com.urlshortener.dto.request;

import java.time.LocalDateTime;

public class ClickEventMessage {

    private String shortCode;
    private String ipAddress;
    private LocalDateTime clickedAt;

    public ClickEventMessage() {
    }

    public ClickEventMessage(String shortCode, String ipAddress, LocalDateTime clickedAt) {
        this.shortCode = shortCode;
        this.ipAddress = ipAddress;
        this.clickedAt = clickedAt;
    }

    public String getShortCode() { return shortCode; }
    public String getIpAddress() { return ipAddress; }
    public LocalDateTime getClickedAt() { return clickedAt; }

    public void setShortCode(String shortCode) { this.shortCode = shortCode; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public void setClickedAt(LocalDateTime clickedAt) { this.clickedAt = clickedAt; }
}