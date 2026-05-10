package com.urlshortener.service;

import com.urlshortener.dto.request.ShortenRequest;
import com.urlshortener.dto.response.ShortenResponse;
import com.urlshortener.entity.Url;
import com.urlshortener.exception.UrlExpiredException;
import com.urlshortener.exception.UrlNotFoundException;
import com.urlshortener.repository.UrlRepository;
import com.urlshortener.util.Base62Encoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final Base62Encoder base62Encoder;

    public UrlServiceImpl(UrlRepository urlRepository, Base62Encoder base62Encoder) {
        this.urlRepository = urlRepository;
        this.base62Encoder = base62Encoder;
    }

    public ShortenResponse shortenUrl(ShortenRequest request) {

        //Generating Unique Short Code
        String shortCode;
        do {
            shortCode = base62Encoder.encode();
        } while (urlRepository.existsByShortCode(shortCode));

        //Build and save entity
        Url url = new Url();
        url.setOriginalUrl(request.getOriginalUrl());
        url.setShortCode(shortCode);
        url.setCreatedAt(LocalDateTime.now());

        //Setting Expiry if provided
        if (request.getExpiresAt() != null && !request.getExpiresAt().isEmpty()) {
            url.setExpiresAt(LocalDateTime.parse(request.getExpiresAt()));
        }

        urlRepository.save(url);

        String shortUrl = "http://localhost:8080/" + shortCode;

        return new ShortenResponse(shortUrl, request.getOriginalUrl(), request.getExpiresAt());
    }

    public String getOriginalUrl(String shortCode) {
        Url url = urlRepository.findByShortCode(shortCode).orElseThrow(() -> new UrlNotFoundException(shortCode));

        //Check Expiry
        if (url.getExpiresAt() != null && LocalDateTime.now().isAfter(url.getExpiresAt())) {
            throw new UrlExpiredException(shortCode);
        }

        return url.getOriginalUrl();
    }
}
