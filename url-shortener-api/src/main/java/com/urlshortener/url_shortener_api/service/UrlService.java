package com.urlshortener.url_shortener_api.service;

import com.urlshortener.url_shortener_api.dto.request.ShortenRequest;
import com.urlshortener.url_shortener_api.dto.response.ShortenResponse;

public interface UrlService {

    ShortenResponse shortenUrl(ShortenRequest request);

    String getOriginalUrl(String shortCode0);


}
