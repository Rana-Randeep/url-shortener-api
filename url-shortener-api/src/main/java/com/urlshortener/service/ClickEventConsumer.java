package com.urlshortener.service;

import com.urlshortener.dto.request.ClickEventMessage;
import com.urlshortener.model.ClickEvent;
import com.urlshortener.repository.ClickEventRepository;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ClickEventConsumer {

    private final ClickEventRepository clickEventRepository;
    private final ObjectMapper objectMapper;

    public ClickEventConsumer(ClickEventRepository clickEventRepository, ObjectMapper objectMapper) {
        this.clickEventRepository = clickEventRepository;
        this.objectMapper = objectMapper;
    }

    @SqsListener("click-events-queue")
    public void consume(String payLoad) {
        try {
            ClickEventMessage message = objectMapper.readValue(payLoad, ClickEventMessage.class);

            ClickEvent clickEvent = new ClickEvent(
                    message.getShortCode(),
                    message.getClickedAt(),
                    message.getIpAddress()
            );

            clickEventRepository.save(clickEvent);
            System.out.println("Click event saved to DB: " + message.getShortCode());
        }
        catch (Exception e) {
            System.out.println("Failed to process click event: " + e.getMessage());
        }
    }
}
