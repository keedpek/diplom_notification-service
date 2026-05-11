package com.example.notification_service.scheduler;

import com.example.notification_service.config.OutboxConfig;
import com.example.notification_service.entity.OutboxEvent;
import com.example.notification_service.repository.NotificationOutboxRepository;
import com.example.notification_service.service.outbox.OutboxEventProcessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OutboxScheduler extends AbstractBatchScheduler<OutboxEvent> {

  private final NotificationOutboxRepository notificationOutboxRepository;
  private final OutboxEventProcessor outboxEventProcessor;
  private final OutboxConfig outboxConfig;

  public OutboxScheduler(NotificationOutboxRepository notificationOutboxRepository,
                         OutboxEventProcessor outboxEventProcessor,
                         OutboxConfig outboxConfig) {
    super(outboxConfig.getThreadPoolSize(), "OutboxScheduler");
    this.notificationOutboxRepository = notificationOutboxRepository;
    this.outboxEventProcessor = outboxEventProcessor;
    this.outboxConfig = outboxConfig;
  }

  @Scheduled(fixedRateString = "${app.outbox.scheduler-interval-ms}")
  public void processEvents() {
    List<OutboxEvent> events = notificationOutboxRepository.lockBatch(outboxConfig.getBatchSize());
    processBatch(events, outboxEventProcessor::process);
  }
}
