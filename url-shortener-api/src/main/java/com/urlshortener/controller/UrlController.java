package com.urlshortener.controller;

import com.urlshortener.dto.request.ClickEventMessage;
import com.urlshortener.dto.request.ShortenRequest;
import com.urlshortener.dto.response.ShortenResponse;
import com.urlshortener.service.ClickEventProducer;
import com.urlshortener.service.UrlService;
import com.urlshortener.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
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

    @PostMapping("/shorten")
    public ResponseEntity<ShortenResponse> shortenUrl(
            @Valid @RequestBody ShortenRequest request) {
        String email = jwtUtil.getCurrentUserEmail();
        ShortenResponse response = urlService.shortenUrl(request, email);
        return ResponseEntity.ok(response);
    }

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
}