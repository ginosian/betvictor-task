package com.task.betvictortask.kafka;

import com.task.betvictortask.controller.dto.TextProcessingStats;
import com.task.betvictortask.repository.ProcessedWordsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final ProcessedWordsService processedWordsService;

    @KafkaListener(
            topics = "${spring.kafka.topic.words-processed}",
            groupId = "${spring.kafka.consumer.group-id}",
            concurrency = "${spring.kafka.consumer.properties.concurrency}")
    public void consumeMessage(ConsumerRecord<String, KafkaMessage> record, Acknowledgment acknowledgment) {
        try {
            log.info("Consumed message from topic:'{}', partition:'{}', key:'{}', value:'{}', correlationId:'{}'",
                    record.topic(),
                    record.partition(),
                    record.key(),
                    record.value(),
                    record.headers().lastHeader(KafkaHeaders.CORRELATION_ID));

            acknowledgment.acknowledge();
            log.info("Message acknowledged successfully");

            save(record.value());
        } catch (Exception e) {
            log.error("Error processing Kafka message from partition:'{}'", record.partition(), e);
        }
    }

    private void save(KafkaMessage message) {
        processedWordsService.save(new TextProcessingStats(
                message.getFreqWord(),
                message.getAvgParagraphSize(),
                message.getAvgParagraphProcessingTime(),
                message.getTotalProcessingTime()
        ));
        log.info("Processed words saved to database");
    }
}

