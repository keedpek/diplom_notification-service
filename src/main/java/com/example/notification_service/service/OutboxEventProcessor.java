package com.example.notification_service.service;

import com.example.notification_service.entity.OutboxEvent;

public interface OutboxEventProcessor {
  void process(OutboxEvent outboxEvent);
}
