package com.example.notification_service.service;

import com.example.notification_service.DTO.NotificationDto;
import com.example.notification_service.messaging.event.payload.*;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
  void notifyCreatorOnRequestCreate(RequestCreatedEventDto eventDto);
  void notifyCreatorOnRequestAssigned(RequestAssignedEventDto e);
  void notifyExecutor(RequestAssignedEventDto eventDto);
  void notifyStatusChanged(UUID userId, RequestStatusChangedEventDto eventDto);
  void notifySlaWarning(RequestSlaWarningEventDto eventDto);
  void notifySlaViolation(RequestSlaViolationEventDto eventDto);
  List<NotificationDto> getUserNotifications(UUID userId);
  void markNotificationAsRead(UUID id);
}
