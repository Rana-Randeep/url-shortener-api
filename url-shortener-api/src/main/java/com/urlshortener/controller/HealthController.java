package com.urlshortener.controller;

import com.urlshortener.service.S3Service;
import com.urlshortener.util.QrCodeGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/api")
public class HealthController {


    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("URL Shortener API is running");
    }

}
