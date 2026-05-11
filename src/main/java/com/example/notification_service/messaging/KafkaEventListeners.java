package com.example.notification_service.messaging;

import com.example.notification_service.messaging.event.EventDto;
import com.example.notification_service.messaging.event.payload.RequestAssignedEventDto;
import com.example.notification_service.messaging.event.payload.RequestCreatedEventDto;
import com.example.notification_service.messaging.event.payload.RequestStatusChangedEventDto;
import com.example.notification_service.service.inbox.InboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventListeners {

  private final InboxService inboxService;

  @KafkaListener(topics = "request-created")
  public void onRequestCreated(
          EventDto<RequestCreatedEventDto> event,
          Acknowledgment acknowledgment
  ) {
    log.info("Получено событие о создании запроса: eventId={}", event.getEventId());
    inboxService.saveIfNew(event);
    acknowledgment.acknowledge();
  }

  @KafkaListener(topics = "request-assigned")
  public void onRequestAssigned(
          EventDto<RequestAssignedEventDto> event,
          Acknowledgment acknowledgment
  ) {
    log.info("Получено событие о назначении запроса: eventId={}", event.getEventId());
    inboxService.saveIfNew(event);
    acknowledgment.acknowledge();
  }

  @KafkaListener(topics = "request-status-changed")
  public void onRequestStatusChanged(
          EventDto<RequestStatusChangedEventDto> event,
          Acknowledgment acknowledgment
  ) {
    log.info("Получено событие о изменении статуса запроса: eventId={}", event.getEventId());
    inboxService.saveIfNew(event);
    acknowledgment.acknowledge();
  }
}
