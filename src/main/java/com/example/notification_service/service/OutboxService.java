package com.example.notification_service.service;

import com.example.notification_service.entity.Notification;

public interface OutboxService {
  void save(Notification notification);
}
