package com.urlshortener.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "click_events")
public class ClickEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String shortCode;

    @Column(nullable = false)
    private LocalDateTime clickedAt;

    private String ipAddress;

    public ClickEvent() {
    }

    public ClickEvent(String shortCode, LocalDateTime clickedAt, String ipAddress) {
        this.shortCode = shortCode;
        this.clickedAt = clickedAt;
        this.ipAddress = ipAddress;
    }

    public Long getId() { return id; }
    public String getShortCode() { return shortCode; }
    public LocalDateTime getClickedAt() { return clickedAt; }
    public String getIpAddress() { return ipAddress; }

    public void setId(Long id) { this.id = id; }
    public void setShortCode(String shortCode) { this.shortCode = shortCode; }
    public void setClickedAt(LocalDateTime clickedAt) { this.clickedAt = clickedAt; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
}