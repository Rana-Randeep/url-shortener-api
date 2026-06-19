package com.urlshortener.service;

import com.urlshortener.dto.response.AnalyticsResponse;
import org.springframework.stereotype.Service;

@Service
public interface AnalyticsService {

    AnalyticsResponse getAnalytics(String shortCode);
}
