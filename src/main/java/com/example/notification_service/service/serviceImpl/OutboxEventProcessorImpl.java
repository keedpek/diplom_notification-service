package com.example.notification_service.service.serviceImpl;

import com.example.notification_service.entity.DeliveryLog;
import com.example.notification_service.entity.Notification;
import com.example.notification_service.entity.OutboxEvent;
import com.example.notification_service.enums.NotificationOutboxStatus;
import com.example.notification_service.repository.NotificationDeliveryLogRepository;
import com.example.notification_service.service.NotificationDispatcher;
import com.example.notification_service.service.OutboxEventProcessor;
import com.example.notification_service.util.OutboxConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxEventProcessorImpl implements OutboxEventProcessor {

  private final EntityManager entityManager;
  private final NotificationDispatcher notificationDispatcher;
  private final NotificationDeliveryLogRepository deliveryLogRepository;
  private final ObjectMapper objectMapper;

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void process(OutboxEvent unmanagedEvent) {
    OutboxEvent event = entityManager.merge(unmanagedEvent);

    try {
      Notification notification = objectMapper.convertValue(event.getPayload(), Notification.class);

      if (deliveryLogRepository.existsById(notification.getId())) {
        log.debug("Событие уже было обработано, id={}", event.getId());
        markSucceeded(event);
        return;
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
      if (event != null) {
        log.info("Ошибка при обработке события: id={}, message={}", event.getId(), e.getMessage());
        handleFailure(event);
      }
    }
  }


  private void markSucceeded(OutboxEvent event) {
    event.setStatus(NotificationOutboxStatus.SENT);
  }

  private void handleFailure(OutboxEvent event) {
    int retries = event.getRetryCount() + 1;

    if (retries > OutboxConstants.MAX_RETRIES) {
      event.setStatus(NotificationOutboxStatus.DEAD);
    } else {
      event.setStatus(NotificationOutboxStatus.NEW);
      event.setNextRetryAt(calculateNextRetry(retries));
    }

    event.setRetryCount(retries);
  }

  private LocalDateTime calculateNextRetry(int retryCount) {
    return LocalDateTime.now().plusSeconds((long) Math.pow(2, retryCount));
  }
}
