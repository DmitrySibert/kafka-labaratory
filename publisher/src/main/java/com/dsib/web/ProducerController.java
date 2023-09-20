package com.dsib.web;

import com.dsib.web.dto.Message;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/producer")
public class ProducerController {

  private final KafkaTemplate<String, String> kafkaTemplate;

  public ProducerController(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @PostMapping("/send")
  public void sendMessage(@RequestBody Message msg) {
    CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(msg.getTopic(), msg.getMsg());
    future.whenComplete((result, ex) -> {
      if (ex == null) {
        System.out.println("Sent message=[" + msg.getMsg() +
          "] with offset=[" + result.getRecordMetadata().offset() + "]");
      } else {
        System.out.println("Unable to send message=[" +
          msg.getMsg() + "] due to : " + ex.getMessage());
      }
    });
  }
}
