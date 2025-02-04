package com.task.betvictortask.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.errors.TopicExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class KafkaTopicInitializer implements ApplicationRunner {

  private final KafkaAdmin kafkaAdmin;

    @Autowired
    public KafkaTopicInitializer(KafkaAdmin kafkaAdmin) {
        this.kafkaAdmin = kafkaAdmin;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        final String topicName = "words.processed";
        final int partitions = 4;
        final short replicationFactor = 3;

        log.info("Starting Kafka topic initialization for '{}'", topicName);

        Map<String, Object> configs = kafkaAdmin.getConfigurationProperties();

        try (AdminClient adminClient = AdminClient.create(configs)) {
            ListTopicsOptions listTopicsOptions = new ListTopicsOptions().listInternal(false);
            Set<String> topics = adminClient.listTopics(listTopicsOptions).names().get();

            if (!topics.contains(topicName)) {
                log.info("Topic '{}' does not exist. Creating with '{}' partitions and replication factor '{}'.",
                        topicName, partitions, replicationFactor);

                NewTopic newTopic = new NewTopic(topicName, partitions, replicationFactor);
                try {
                    CreateTopicsResult createTopicsResult = adminClient.createTopics(Collections.singleton(newTopic));
                    createTopicsResult.all().get();
                } catch (ExecutionException e) {
                    if (!(e.getCause() instanceof TopicExistsException)) {
                        throw e;
                    }
                }

                boolean created = false;
                int attempts = 0;
                while (attempts < 10) {
                    topics = adminClient.listTopics(listTopicsOptions).names().get();
                    if (topics.contains(topicName)) {
                        created = true;
                        break;
                    }
                    Thread.sleep(1000);
                    attempts++;
                }
                if (!created) {
                    log.error("Topic '" + topicName + "' was not created after waiting.");
                    throw new RuntimeException("Topic '" + topicName + "' was not created after waiting.");
                }
                log.info("Topic '{}' successfully created with {} partitions and replication factor {}.",
                        topicName, partitions, replicationFactor);
            } else {
                DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(Collections.singleton(topicName));
                TopicDescription topicDescription = describeTopicsResult.values().get(topicName).get();
                int currentPartitions = topicDescription.partitions().size();
                int currentReplicationFactor = topicDescription.partitions().get(0).replicas().size();

                if (currentReplicationFactor != replicationFactor) {
                    log.warn("Topic '{}' has replication factor '{}' but required is '{}'.",
                            topicName, currentReplicationFactor, replicationFactor);
                }

                if (currentPartitions < partitions) {
                    log.info("Increasing partition count of '{}' from '{}' to '{}'.",
                            topicName, currentPartitions, partitions);
                    Map<String, NewPartitions> newPartitions = new HashMap<>();
                    newPartitions.put(topicName, NewPartitions.increaseTo(partitions));
                    adminClient.createPartitions(newPartitions).all().get();
                    log.info("Topic '{}' partition count increased to '{}'.", topicName, partitions);
                } else {
                    log.info("Topic '{}' already exists with '{}' partitions.", topicName, currentPartitions);
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Failed to initialize topic '{}'.", topicName, e);
            throw new RuntimeException("Failed to initialize topic '" + topicName + "'.", e);
        }
    }
}

