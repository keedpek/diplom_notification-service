package com.example.notification_service.service.outbox;

import com.example.notification_service.entity.Notification;

public interface OutboxService {
  void save(Notification notification);
}
