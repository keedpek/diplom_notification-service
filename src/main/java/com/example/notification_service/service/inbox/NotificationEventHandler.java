package com.example.notification_service.service.inbox;

import com.example.notification_service.enums.EventType;
import com.fasterxml.jackson.databind.JsonNode;

public interface NotificationEventHandler {
  void handle(EventType eventType, JsonNode payload);
}
