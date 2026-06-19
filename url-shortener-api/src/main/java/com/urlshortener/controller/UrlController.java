package com.urlshortener.controller;

import com.urlshortener.dto.request.ClickEventMessage;
import com.urlshortener.dto.request.ShortenRequest;
import com.urlshortener.dto.response.ShortenResponse;
import com.urlshortener.service.ClickEventProducer;
import com.urlshortener.service.UrlService;
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

    public UrlController(UrlService urlService, ClickEventProducer clickEventProducer) {
        this.urlService = urlService;
        this.clickEventProducer = clickEventProducer;
    }

    @PostMapping("/shorten")
    public ResponseEntity<ShortenResponse> shortenUrl(@Valid @RequestBody ShortenRequest request) {
        ShortenResponse response = urlService.shortenUrl(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode, HttpServletRequest request) {
        String originalUrl = urlService.getOriginalUrl(shortCode);

        //Publish click event to SQS
        ClickEventMessage message = new ClickEventMessage(
                shortCode,
                request.getRemoteAddr(),
                LocalDateTime.now()
        );
        clickEventProducer.publish(message);

        return ResponseEntity.status(HttpStatus.FOUND) //HttpStatus.FOUND = HTTP 302 = standard redirect response.
                .location(URI.create(originalUrl))
                .build();
    }

}
