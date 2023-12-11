package com.example.democonsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Instant;

@SpringBootApplication
public class DemoConsumerApplication implements ApplicationRunner {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topic}")
    private String kafkaTopic;

    @Value("${spring.kafka.role}")
    private String kafkaRole;

    public static void main(String[] args) {
        SpringApplication.run(DemoConsumerApplication.class, args).close();
    }

    @Override
    public void run(ApplicationArguments args) {
        sendRecordToTopic(kafkaTopic, String.valueOf(Instant.now()));
        sendRecordToTopic(kafkaTopic, "Heart");
        sendRecordToTopic(kafkaTopic, "Logs");
        sendRecordToTopic(kafkaTopic, "Event Data");
        sendRecordToTopic(kafkaTopic, "Stream Processing");
        sendRecordToTopic(kafkaTopic, "and Data Integration");
    }

    private void sendRecordToTopic(String topic, String value) {
        try {
            kafkaTemplate.send(topic, value);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
