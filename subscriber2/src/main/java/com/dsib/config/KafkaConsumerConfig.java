package com.dsib.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@EnableKafka
public class KafkaConsumerConfig {

  @Value(value = "${spring.kafka.bootstrap-servers}")
  private String bootstrapAddress;
  @Value(value = "${spring.kafka.consumer-group}")
  private String consumerGroup;
  @Value(value = "${spring.kafka.topic}")
  private String topic;

  @Bean
  public ConsumerFactory<String, String> consumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
      bootstrapAddress);
    props.put(
      ConsumerConfig.GROUP_ID_CONFIG,
      consumerGroup);
    props.put(
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
      StringDeserializer.class);
    props.put(
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
      StringDeserializer.class);
    return new DefaultKafkaConsumerFactory<>(props);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, String> factory =
      new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConcurrency(1);
    factory.setConsumerFactory(consumerFactory());
    createListeners(factory);
    return factory;
  }

  private final MessageListener<String, String> handler = record -> {
    log.info("Received thread = {}, msg = {}", Thread.currentThread().getName(), record.value());
  };
  private void createListeners(ConcurrentKafkaListenerContainerFactory<String, String> factory) {
    ConcurrentMessageListenerContainer<String, String> container = factory.createContainer(topic);
    container.getContainerProperties().setGroupId(consumerGroup);
    container.setupMessageListener(handler);
    container.start();
  }
}
