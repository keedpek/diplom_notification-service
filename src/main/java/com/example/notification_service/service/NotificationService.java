package com.example.notification_service.service;

import com.example.notification_service.DTO.NotificationDto;
import com.example.notification_service.DTO.events.RequestAssignedEventDto;
import com.example.notification_service.DTO.events.RequestSlaViolationEventDto;
import com.example.notification_service.DTO.events.RequestSlaWarningEventDto;
import com.example.notification_service.DTO.events.RequestStatusChangedEventDto;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
  void notifyExecutor(RequestAssignedEventDto eventDto);
  void notifyStatusChanged(RequestStatusChangedEventDto eventDto);
  void notifySlaWarning(RequestSlaWarningEventDto eventDto);
  void notifySlaViolation(RequestSlaViolationEventDto eventDto);
  List<NotificationDto> getUserNotifications(UUID userId);
  void markNotificationAsRead(UUID id);
}
