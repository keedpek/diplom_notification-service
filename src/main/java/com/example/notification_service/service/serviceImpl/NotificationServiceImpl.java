package com.example.notification_service.service.serviceImpl;

import com.example.notification_service.DTO.NotificationDto;
import com.example.notification_service.DTO.events.RequestAssignedEventDto;
import com.example.notification_service.DTO.events.RequestSlaViolationEventDto;
import com.example.notification_service.DTO.events.RequestSlaWarningEventDto;
import com.example.notification_service.DTO.events.RequestStatusChangedEventDto;
import com.example.notification_service.entity.Notification;
import com.example.notification_service.enums.NotificationTypes;
import com.example.notification_service.exceptions.NotFoundException;
import com.example.notification_service.mapper.NotificationMapper;
import com.example.notification_service.repository.NotificationRepository;
import com.example.notification_service.service.NotificationDispatcher;
import com.example.notification_service.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;
  private final NotificationDispatcher dispatcher;

  @Override
  @Transactional
  public void notifyExecutor(RequestAssignedEventDto e) {
    Notification notification = Notification.builder()
            .userId(e.getAssignedUserId())
            .requestId(e.getRequestId())
            .type(NotificationTypes.ASSIGNED)
            .title("Новая заявка!")
            .message("Вам назначена заявка: " + e.getTitle())
            .isRead(false)
            .createdAt(LocalDateTime.now())
            .build();

    notificationRepository.save(notification);
    dispatcher.dispatch(notification);
  }

  @Override
  @Transactional
  public void notifyStatusChanged(RequestStatusChangedEventDto e) {
    Notification notification = Notification.builder()
            .userId(e.getUserId())
            .requestId(e.getRequestId())
            .type(NotificationTypes.STATUS_CHANGED)
            .title("Статус заявки изменен")
            .message("Статус изменен на: " + e.getStatus())
            .isRead(false)
            .createdAt(LocalDateTime.now())
            .build();

    notificationRepository.save(notification);
    dispatcher.dispatch(notification);
  }

  @Override
  @Transactional
  public void notifySlaWarning(RequestSlaWarningEventDto e) {
    Notification notification = Notification.builder()
            .userId(e.getUserId())
            .requestId(e.getRequestId())
            .type(NotificationTypes.SLA_WARNING)
            .title("Приближается дедлайн")
            .message(buildSlaWarningMessage(e))
            .isRead(false)
            .createdAt(LocalDateTime.now())
            .build();

    notificationRepository.save(notification);
    dispatcher.dispatchCritical(notification);
  }

  @Override
  @Transactional
  public void notifySlaViolation(RequestSlaViolationEventDto e) {
    Notification notification = Notification.builder()
            .userId(e.getUserId())
            .requestId(e.getRequestId())
            .type(NotificationTypes.SLA_VIOLATION)
            .title("Приближается дедлайн")
            .message("Срок выполнения заявки \"" + e.getTitle() + "\" нарушен!")
            .isRead(false)
            .createdAt(LocalDateTime.now())
            .build();

    notificationRepository.save(notification);
    dispatcher.dispatchCritical(notification);
  }

  @Override
  public List<NotificationDto> getUserNotifications(UUID userId) {
    if (userId == null) {
      throw new IllegalArgumentException("Идентификатор пользователя обязателен");
    }
    return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
            .map(notificationMapper::toDto)
            .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void markNotificationAsRead(UUID id) {
    Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Уведомление не найдено"));
    notification.setIsRead(true);
  }

  private String buildSlaWarningMessage(RequestSlaWarningEventDto e) {
    StringBuilder sb = new StringBuilder();
    sb.append("По заявке \"")
            .append(e.getTitle())
            .append("\" скоро истекает срок выполнения.");

    if (e.getMinutesLeft() != null) {
      sb.append(" Осталось ")
              .append(e.getMinutesLeft())
              .append(" минут.");
    }

    return sb.toString();
  }
}
