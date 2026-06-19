package com.urlshortener.service;

import com.urlshortener.dto.response.AnalyticsResponse;
import com.urlshortener.exception.UrlNotFoundException;
import com.urlshortener.model.ClickEvent;
import com.urlshortener.repository.ClickEventRepository;
import com.urlshortener.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalyticsServiceImpl implements AnalyticsService{

    private final ClickEventRepository clickEventRepository;
    private final UrlRepository urlRepository;

    public AnalyticsServiceImpl(ClickEventRepository clickEventRepository, UrlRepository urlRepository) {
        this.clickEventRepository = clickEventRepository;
        this.urlRepository = urlRepository;
    }

    public AnalyticsResponse getAnalytics(String shortCode) {

        urlRepository.findByShortCode(shortCode)
                .orElseThrow( () -> new UrlNotFoundException(shortCode));

        long totalClicks = clickEventRepository.countByShortCode(shortCode);

        List<ClickEvent> events = clickEventRepository.findByShortCode(shortCode);

        List<AnalyticsResponse.ClickDetail> recentClicks = events.stream()
                .map(event -> new AnalyticsResponse.ClickDetail(
                        event.getIpAddress(),
                        event.getClickedAt().toString()
                ))
                .collect(Collectors.toList());

        return new AnalyticsResponse(shortCode, totalClicks, recentClicks);
    }
}
