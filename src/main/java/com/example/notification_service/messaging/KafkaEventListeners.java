package com.example.notification_service.messaging;

import com.example.notification_service.messaging.event.EventDto;
import com.example.notification_service.messaging.event.payload.RequestAssignedEventDto;
import com.example.notification_service.messaging.event.payload.RequestCreatedEventDto;
import com.example.notification_service.messaging.event.payload.RequestStatusChangedEventDto;
import com.example.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventListeners {

  private final NotificationService notificationService;

  @KafkaListener(topics = "request.created")
  public void onRequestCreated(
          EventDto<RequestCreatedEventDto> event,
          Acknowledgment acknowledgment
  ) {
    log.info("Получено событие о создании запроса: eventId={}", event.getEventId());
    RequestCreatedEventDto payload = event.getPayload();
    log.debug("Информация о событии: {}", payload);
    notificationService.notifyCreatorOnRequestCreate(payload);
    acknowledgment.acknowledge();
  }

  @KafkaListener(topics = "request.assigned")
  public void onRequestAssigned(
          EventDto<RequestAssignedEventDto> event,
          Acknowledgment acknowledgment
  ) {
    log.info("Получено событие о назначении запроса: eventId={}", event.getEventId());
    RequestAssignedEventDto payload = event.getPayload();
    notificationService.notifyExecutor(payload);
    notificationService.notifyCreatorOnRequestAssigned(payload);
    acknowledgment.acknowledge();
  }

  @KafkaListener(topics = "request.status.changed")
  public void onRequestStatusChanged(
          EventDto<RequestStatusChangedEventDto> event,
          Acknowledgment acknowledgment
  ) {
    log.info("Получено событие о изменении статуса запроса: eventId={}", event.getEventId());
    RequestStatusChangedEventDto payload = event.getPayload();
    List<UUID> users = payload.getUserIds();
    if (users != null) {
      for (UUID user : users) {
        notificationService.notifyStatusChanged(user, payload);
      }
    }
    acknowledgment.acknowledge();
  }
}
