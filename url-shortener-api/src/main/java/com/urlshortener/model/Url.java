package com.urlshortener.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "urls")
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048)
    private String originalUrl;

    @Column(nullable = false, unique = true)
    private String shortCode;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Url() {
    }

    public Url(String originalUrl, String shortCode,
               LocalDateTime createdAt, LocalDateTime expiresAt, User user) {
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.user = user;
    }

    public Long getId() { return id; }
    public String getOriginalUrl() { return originalUrl; }
    public String getShortCode() { return shortCode; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public User getUser() { return user; }

    public void setId(Long id) { this.id = id; }
    public void setOriginalUrl(String originalUrl) { this.originalUrl = originalUrl; }
    public void setShortCode(String shortCode) { this.shortCode = shortCode; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public void setUser(User user) { this.user = user; }
}