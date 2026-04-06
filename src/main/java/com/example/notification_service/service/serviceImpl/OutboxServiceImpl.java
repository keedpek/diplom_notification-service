package com.example.notification_service.service.serviceImpl;

import com.example.notification_service.entity.Notification;
import com.example.notification_service.entity.OutboxEvent;
import com.example.notification_service.enums.NotificationOutboxStatus;
import com.example.notification_service.repository.NotificationOutboxRepository;
import com.example.notification_service.service.OutboxService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxServiceImpl implements OutboxService {

  private final NotificationOutboxRepository notificationOutboxRepository;
  private final ObjectMapper objectMapper;

  @Override
  public void save(Notification notification) {
    log.info("Сохранение события в outbox: id={}", notification.getId());
    try {
      OutboxEvent event = OutboxEvent.builder()
              .id(UUID.randomUUID())
              .payload(objectMapper.valueToTree(notification))
              .status(NotificationOutboxStatus.NEW)
              .retryCount(0)
              .locked(false)
              .createdAt(LocalDateTime.now())
              .build();
      notificationOutboxRepository.save(event);
    } catch (Exception e) {
      log.error("Ошибка при сохранении события в outbox: id={}, message={}", notification.getId(), e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
