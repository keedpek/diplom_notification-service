package com.example.notification_service.scheduler;

import com.example.notification_service.config.InboxConfig;
import com.example.notification_service.entity.InboxEvent;
import com.example.notification_service.repository.NotificationInboxRepository;
import com.example.notification_service.service.inbox.InboxEventProcessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InboxScheduler extends AbstractBatchScheduler<InboxEvent> {

  private final NotificationInboxRepository notificationInboxRepository;
  private final InboxEventProcessor inboxEventProcessor;
  private final InboxConfig inboxConfig;

  public InboxScheduler(NotificationInboxRepository notificationInboxRepository,
                        InboxEventProcessor inboxEventProcessor,
                        InboxConfig inboxConfig) {
    super(inboxConfig.getThreadPoolSize(), "InboxScheduler");
    this.notificationInboxRepository = notificationInboxRepository;
    this.inboxEventProcessor = inboxEventProcessor;
    this.inboxConfig = inboxConfig;
  }

  @Scheduled(fixedRateString = "${app.inbox.scheduler-interval-ms}")
  public void processEvents() {
    List<InboxEvent> events = notificationInboxRepository.lockBatchForProcessing(inboxConfig.getBatchSize());
    processBatch(events, inboxEventProcessor::process);
  }
}
