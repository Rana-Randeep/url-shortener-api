package com.urlshortener.service;

import com.urlshortener.dto.request.ShortenRequest;
import com.urlshortener.dto.response.ShortenResponse;

public interface UrlService {

    ShortenResponse shortenUrl(ShortenRequest request, String email);

    String getOriginalUrl(String shortCode);
}