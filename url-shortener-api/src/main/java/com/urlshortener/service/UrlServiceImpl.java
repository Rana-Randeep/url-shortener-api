package com.urlshortener.service;

import com.urlshortener.dto.request.ShortenRequest;
import com.urlshortener.dto.response.ShortenResponse;
import com.urlshortener.exception.UrlExpiredException;
import com.urlshortener.exception.UrlNotFoundException;
import com.urlshortener.model.Url;
import com.urlshortener.model.User;
import com.urlshortener.repository.UrlRepository;
import com.urlshortener.repository.UserRepository;
import com.urlshortener.service.UrlService;
import com.urlshortener.util.Base62Encoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final UserRepository userRepository;
    private final Base62Encoder base62Encoder;

    public UrlServiceImpl(UrlRepository urlRepository,
                          UserRepository userRepository,
                          Base62Encoder base62Encoder) {
        this.urlRepository = urlRepository;
        this.userRepository = userRepository;
        this.base62Encoder = base62Encoder;
    }

    @Override
    public ShortenResponse shortenUrl(ShortenRequest request, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String shortCode;
        do {
            shortCode = base62Encoder.encode();
        } while (urlRepository.existsByShortCode(shortCode));

        Url url = new Url();
        url.setOriginalUrl(request.getOriginalUrl());
        url.setShortCode(shortCode);
        url.setCreatedAt(LocalDateTime.now());
        url.setUser(user);

        if (request.getExpiresAt() != null && !request.getExpiresAt().isEmpty()) {
            url.setExpiresAt(LocalDateTime.parse(request.getExpiresAt()));
        }

        urlRepository.save(url);

        String shortUrl = "http://localhost:8080/api/" + shortCode;
        return new ShortenResponse(shortUrl, request.getOriginalUrl(),
                request.getExpiresAt());
    }

    @Override
    public String getOriginalUrl(String shortCode) {

        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException(shortCode));

        if (url.getExpiresAt() != null && LocalDateTime.now().isAfter(url.getExpiresAt())) {
            throw new UrlExpiredException(shortCode);
        }

        return url.getOriginalUrl();
    }
}