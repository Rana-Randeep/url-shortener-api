package com.urlshortener.controller;

import com.urlshortener.dto.request.ClickEventMessage;
import com.urlshortener.dto.request.ShortenRequest;
import com.urlshortener.dto.response.ShortenResponse;
import com.urlshortener.dto.response.UrlResponse;
import com.urlshortener.service.ClickEventProducer;
import com.urlshortener.service.UrlService;
import com.urlshortener.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "URL Shortener", description = "Shorten, redirect and manage URLs")
public class UrlController {

    private final UrlService urlService;
    private final ClickEventProducer clickEventProducer;
    private final JwtUtil jwtUtil;

    public UrlController(UrlService urlService,
                         ClickEventProducer clickEventProducer,
                         JwtUtil jwtUtil) {
        this.urlService = urlService;
        this.clickEventProducer = clickEventProducer;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "Shorten a URL and generate QR code",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @PostMapping("/shorten")
    public ResponseEntity<ShortenResponse> shortenUrl(
            @Valid @RequestBody ShortenRequest request) {
        String email = jwtUtil.getCurrentUserEmail();
        ShortenResponse response = urlService.shortenUrl(request, email);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Redirect to original URL using short code")
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode,
                                         HttpServletRequest request) {
        String originalUrl = urlService.getOriginalUrl(shortCode);
        ClickEventMessage message = new ClickEventMessage(
                shortCode,
                request.getRemoteAddr(),
                LocalDateTime.now()
        );
        clickEventProducer.publish(message);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }

    @Operation(summary = "Get all URLs of logged in user",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @GetMapping("/my-urls")
    public ResponseEntity<List<UrlResponse>> getMyUrls() {
        String email = jwtUtil.getCurrentUserEmail();
        List<UrlResponse> urls = urlService.getMyUrls(email);
        return ResponseEntity.ok(urls);
    }

    @Operation(summary = "Delete a URL by short code",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @DeleteMapping("/urls/{shortCode}")
    public ResponseEntity<Void> deleteUrl(@PathVariable String shortCode) {
        String email = jwtUtil.getCurrentUserEmail();
        urlService.deleteUrl(shortCode, email);
        return ResponseEntity.noContent().build();
    }
}