package com.urlshortener.service;

import com.urlshortener.dto.request.ShortenRequest;
import com.urlshortener.dto.response.ShortenResponse;
import com.urlshortener.dto.response.UrlResponse;

import java.util.List;

public interface UrlService {

    ShortenResponse shortenUrl(ShortenRequest request, String email);

    String getOriginalUrl(String shortCode);

    List<UrlResponse> getMyUrls(String email);

    void deleteUrl(String shortCode, String email);
}