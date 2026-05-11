package com.example.notification_service.service.inbox.impl;

import com.example.notification_service.entity.InboxEvent;
import com.example.notification_service.repository.NotificationInboxRepository;
import com.example.notification_service.service.inbox.InboxEventProcessor;
import com.example.notification_service.service.inbox.NotificationEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class InboxEventProcessorImpl implements InboxEventProcessor {

  private final NotificationInboxRepository notificationInboxRepository;
  private final NotificationEventHandler notificationEventHandler;

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void process(InboxEvent event) {
    try {
      notificationEventHandler.handle(event.getEventType(), event.getPayload());
      notificationInboxRepository.markSuccess(event.getEventId(), LocalDateTime.now());
      log.info("Inbox событие обработано успешно: eventId={}", event.getEventId());
    } catch (Exception e) {
      log.warn("Ошибка обработки inbox события: eventId={}, message={}", event.getEventId(), e.getMessage());
      notificationInboxRepository.markError(event.getEventId());
    }
  }
}
