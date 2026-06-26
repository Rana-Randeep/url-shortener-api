package com.urlshortener.controller;

import com.urlshortener.dto.response.AnalyticsResponse;
import com.urlshortener.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<AnalyticsResponse> getAnalytics(@PathVariable String shortCode) {
        AnalyticsResponse response = analyticsService.getAnalytics(shortCode);
        return ResponseEntity.ok(response);
    }
}