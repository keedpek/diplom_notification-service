package com.example.notification_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.inbox")
public class InboxConfig {
  private int batchSize = 100;
  private int threadPoolSize = 10;
  private long schedulerIntervalMs = 5000;
}
