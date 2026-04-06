package com.example.notification_service.scheduler;

import com.example.notification_service.entity.DeliveryLog;
import com.example.notification_service.entity.Notification;
import com.example.notification_service.entity.OutboxEvent;
import com.example.notification_service.enums.NotificationOutboxStatus;
import com.example.notification_service.repository.NotificationDeliveryLogRepository;
import com.example.notification_service.repository.NotificationOutboxRepository;
import com.example.notification_service.service.NotificationDispatcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxScheduler {

  private final NotificationDispatcher notificationDispatcher;
  private final NotificationOutboxRepository notificationOutboxRepository;
  private final NotificationDeliveryLogRepository deliveryLogRepository;
  private final ObjectMapper objectMapper;

  private static final int MAX_RETRIES = 5;

  @Scheduled(fixedRate = 5000)
  @Transactional
  public void processEvents() {
    List<OutboxEvent> events = notificationOutboxRepository.lockBatch();
    log.info("Обработка {} событий(-ия)", events.size());

    for (OutboxEvent event : events) {
      try {
        Notification notification = objectMapper.convertValue(event.getPayload(), Notification.class);

        if (deliveryLogRepository.existsById(notification.getId())) {
          log.debug("Событие уже было обработано, id={}", event.getId());
          markSucceeded(event);
          continue;
        }

        notificationDispatcher.dispatch(notification);

        DeliveryLog delivery = DeliveryLog.builder()
                .notificationId(notification.getId())
                .deliveredAt(LocalDateTime.now())
                .build();
        deliveryLogRepository.save(delivery);

        markSucceeded(event);
        log.info("Событие обработано успешно: id={}", event.getId());
      } catch (Exception e) {
        log.info("Ошибка при обработке события: id={}, message={}", event.getId(), e.getMessage());
        handleFailure(event);
      }
    }
  }

  private void markSucceeded(OutboxEvent event) {
    event.setStatus(NotificationOutboxStatus.SENT);
    event.setLocked(false);
  }

  private void handleFailure(OutboxEvent event) {
    int retries = event.getRetryCount() + 1;

    if (retries > MAX_RETRIES) {
      event.setStatus(NotificationOutboxStatus.DEAD);
    } else {
      event.setStatus(NotificationOutboxStatus.NEW);
      event.setNextRetryAt(calculateNextRetry(retries));
    }

    event.setRetryCount(retries);
    event.setLocked(false);
  }

  private LocalDateTime calculateNextRetry(int retryCount) {
    return LocalDateTime.now().plusSeconds((long) Math.pow(2, retryCount));
  }
}
