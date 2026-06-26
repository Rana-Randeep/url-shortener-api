package com.urlshortener.service;

import com.urlshortener.dto.request.ShortenRequest;
import com.urlshortener.dto.response.ShortenResponse;
import com.urlshortener.dto.response.UrlResponse;
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
import java.util.List;
import java.util.stream.Collectors;

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

    public List<UrlResponse> getMyUrls(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Url> urls = urlRepository.findByUser(user);

        return urls.stream()
                .map(url -> new UrlResponse(
                        url.getShortCode(),
                        url.getOriginalUrl(),
                        "http://localhost:8080/api/" + url.getShortCode(),
                        url.getCreatedAt().toString(),
                        url.getExpiresAt() != null ? url.getExpiresAt().toString() : null
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUrl(String shortCode, String email) {

        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException(shortCode));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!url.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized to delete this URL");
        }

        urlRepository.delete(url);
    }
}