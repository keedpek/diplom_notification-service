package com.example.notification_service.service.inbox.impl;

import com.example.notification_service.entity.InboxEvent;
import com.example.notification_service.enums.NotificationInboxStatus;
import com.example.notification_service.messaging.event.EventDto;
import com.example.notification_service.messaging.event.payload.EventDtoPayload;
import com.example.notification_service.repository.NotificationInboxRepository;
import com.example.notification_service.service.inbox.InboxService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class InboxServiceImpl implements InboxService {

  private final NotificationInboxRepository notificationInboxRepository;
  private final ObjectMapper objectMapper;


  @Override
  @Transactional
  public void saveIfNew(EventDto<? extends EventDtoPayload> event) {
    try {
      InboxEvent inboxEvent = InboxEvent.builder()
              .eventId(event.getEventId())
              .eventType(event.getEventType())
              .payload(objectMapper.valueToTree(event.getPayload()))
              .status(NotificationInboxStatus.NEW)
              .createdAt(LocalDateTime.now())
              .build();

      notificationInboxRepository.save(inboxEvent);
      log.info("Событие сохранено в inbox: eventId={}", event.getEventId());
    } catch (DataIntegrityViolationException e) {
      log.info("Дубликат события в inbox: eventId={}", event.getEventId());
    }
  }
}
