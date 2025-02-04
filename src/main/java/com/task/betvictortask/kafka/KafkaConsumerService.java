package com.task.betvictortask.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class KafkaConsumerService {

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

        } catch (Exception e) {
            log.error("Error processing Kafka message from partition:'{}'", record.partition(), e);
              }
    }
}

