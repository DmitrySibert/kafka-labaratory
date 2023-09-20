package com.dsib.web.dto;

import lombok.Data;

@Data
public class Message {
  private String topic;
  private Integer partition;
  private String msg;
}
