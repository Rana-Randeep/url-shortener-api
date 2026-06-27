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

    private final QrCodeGenerator qrCodeGenerator;
    private final S3Service s3Service;

    public HealthController(QrCodeGenerator qrCodeGenerator, S3Service s3Service) {
        this.qrCodeGenerator = qrCodeGenerator;
        this.s3Service = s3Service;
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("URL Shortener API is running");
    }

    @GetMapping("/test-s3")
    public ResponseEntity<String> testS3() throws Exception {
        byte[] qr = qrCodeGenerator.generateQrCode("https://www.google.com");
        String url = s3Service.uploadQrCode("test123", qr);
        return ResponseEntity.ok(url);
    }

}
