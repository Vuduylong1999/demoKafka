package com.example.democonsumer.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;

public class KafkaConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String internalBootstrapServers;

    @Bean
    @Qualifier("internalKafkaTemplate")
    public KafkaTemplate<String, Object> internalKafkaTemplate(
            @Qualifier("internalProducerFactory") ProducerFactory<String, Object> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    @Qualifier("requestSendMessage")
    public KafkaTemplate<String, String> requestSendMessage(
            @Qualifier("requestSendMessageProducerFactory") ProducerFactory<String, String> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    @Qualifier("requestSendMessageProducerFactory")
    public ProducerFactory<String, String> requestSendMessageProducerFactory(
            @Value("#{${spring.kafka.internal-producer.options:{}}}") Map<String, String> options
    ) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, internalBootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        if (nonNull(options)) props.putAll(options);
        return new DefaultKafkaProducerFactory<>(props);
    }

}
