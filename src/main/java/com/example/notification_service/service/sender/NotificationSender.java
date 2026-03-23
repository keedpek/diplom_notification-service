package com.example.notification_service.service.sender;

import com.example.notification_service.entity.Notification;

public interface NotificationSender {
  String getChannelCode();
  void send(Notification notification);
}
