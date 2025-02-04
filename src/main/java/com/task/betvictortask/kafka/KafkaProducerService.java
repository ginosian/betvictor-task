package com.task.betvictortask.kafka;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class KafkaProducerService {

private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    @Value("${spring.kafka.topic.words-processed}")
    private String topicName;

    public KafkaProducerService(KafkaTemplate<String, KafkaMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(KafkaMessage stats) {
        String partitionKey = stats.getFreqWord();
        String requestId = getRequestIdFromContext();
        long timestamp = Instant.now().toEpochMilli();

        log.info("Preparing to send Kafka message with key:'{}', requestId:'{}', timestamp:'{}'",
                partitionKey, requestId, timestamp);

        Message<KafkaMessage> message = MessageBuilder
                .withPayload(stats)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .setHeader(KafkaHeaders.KEY, partitionKey)
                .setHeader(KafkaHeaders.CORRELATION_ID, requestId)
                .setHeader(KafkaHeaders.TIMESTAMP, timestamp)
                .build();

        CompletableFuture<SendResult<String, KafkaMessage>> future = kafkaTemplate.send(message);

        future.whenComplete((result, exception) -> {
            if (exception == null) {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("Message sent successfully to topic:'{}', partition:'{}', offset:'{}', correlationID:'{}'",
                        metadata.topic(), metadata.partition(), metadata.offset(), requestId);
            } else {
                log.error("Failed to send message to Kafka", exception);
                // TODO Implement retry logic here if needed
            }
        });
    }

    private String getRequestIdFromContext() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String requestId = (String) request.getAttribute("REQUEST_ID");

            if (requestId != null) {
                return requestId;
            }
        }
        return UUID.randomUUID().toString();
    }
}
