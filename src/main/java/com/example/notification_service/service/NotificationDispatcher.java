package com.example.notification_service.service;

import com.example.notification_service.entity.Notification;

public interface NotificationDispatcher {
  void dispatch(Notification notification);
  void dispatchCritical(Notification notification);
}
