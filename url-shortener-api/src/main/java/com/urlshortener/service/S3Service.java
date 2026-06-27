package com.urlshortener.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket-name}")
    private String bucketName;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadQrCode(String shortCode, byte[] qrCodeBytes) {

        String key = "qr-" + shortCode + ".png";

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType("image/png")
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(qrCodeBytes));

        return "https://" + bucketName + ".s3.ap-south-1.amazonaws.com/" + key;
    }
}
