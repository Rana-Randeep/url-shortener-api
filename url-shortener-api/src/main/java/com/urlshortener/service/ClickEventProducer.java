package com.urlshortener.service;

import com.urlshortener.dto.request.ClickEventMessage;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ClickEventProducer {

    private final SqsTemplate sqsTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.cloud.aws.sqs.queue.click-events}")
    private String queueName;

    public ClickEventProducer(SqsTemplate sqsTemplate, ObjectMapper objectMapper) {
        this.sqsTemplate = sqsTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(ClickEventMessage message) {
        try {
            String payload = objectMapper.writeValueAsString(message);
            sqsTemplate.send(queueName, payload);
            System.out.println("Click event published to SQS: " + payload);
        } catch (Exception e) {
            System.out.println("Failed to publish click event: " + e.getMessage());
        }
    }
}
