package com.example.notification_service.service.inbox.impl;

import com.example.notification_service.enums.EventType;
import com.example.notification_service.messaging.event.payload.RequestAssignedEventDto;
import com.example.notification_service.messaging.event.payload.RequestCreatedEventDto;
import com.example.notification_service.messaging.event.payload.RequestStatusChangedEventDto;
import com.example.notification_service.service.inbox.NotificationEventHandler;
import com.example.notification_service.service.NotificationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationEventHandlerImpl implements NotificationEventHandler {

  private final NotificationService notificationService;
  private final ObjectMapper objectMapper;

  @Override
  public void handle(EventType eventType, JsonNode payload) {
    switch (eventType) {
      case REQUEST_CREATED -> handleRequestCreated(payload);
      case REQUEST_ASSIGNED -> handleRequestAssigned(payload);
      case STATUS_CHANGED -> handleStatusChanged(payload);
      default -> throw new IllegalArgumentException("Неподдерживаемый тип события: " + eventType);
    }
  }

  private void handleRequestCreated(JsonNode payload) {
    RequestCreatedEventDto dto = objectMapper.convertValue(payload, RequestCreatedEventDto.class);
    notificationService.notifyCreatorOnRequestCreate(dto);
  }

  private void handleRequestAssigned(JsonNode payload) {
    RequestAssignedEventDto dto = objectMapper.convertValue(payload, RequestAssignedEventDto.class);
    notificationService.notifyExecutor(dto);
    notificationService.notifyCreatorOnRequestAssigned(dto);
  }

  private void handleStatusChanged(JsonNode payload) {
    RequestStatusChangedEventDto dto = objectMapper.convertValue(payload, RequestStatusChangedEventDto.class);
    List<UUID> users = dto.getUserIds();
    if (users == null) {
      return;
    }

    for (UUID userId : users) {
      notificationService.notifyStatusChanged(userId, dto);
    }
  }
}
