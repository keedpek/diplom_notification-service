package com.example.notification_service.service.serviceImpl;

import com.example.notification_service.DTO.NotificationDto;
import com.example.notification_service.entity.Notification;
import com.example.notification_service.enums.NotificationType;
import com.example.notification_service.exceptions.NotFoundException;
import com.example.notification_service.mapper.NotificationMapper;
import com.example.notification_service.messaging.event.payload.*;
import com.example.notification_service.repository.NotificationRepository;
import com.example.notification_service.service.NotificationService;
import com.example.notification_service.service.OutboxService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;
  private final OutboxService outboxService;

  @Override
  @Transactional
  public void notifyCreatorOnRequestCreate(RequestCreatedEventDto e) {
    log.info(
            "Уведомление о создании заявки: requestId={}, userId={}",
            e.getRequestId(),
            e.getCreatedByUserId()
    );

    Notification notification = Notification.builder()
            .userId(e.getCreatedByUserId())
            .requestId(e.getRequestId())
            .type(NotificationType.CREATED)
            .title("Заявка создана!")
            .message("Ваша заявка была создана: " + e.getTitle())
            .isRead(false)
            .createdAt(LocalDateTime.now())
            .build();

    notificationRepository.save(notification);
    outboxService.save(notification);
  }

  @Override
  @Transactional
  public void notifyCreatorOnRequestAssigned(RequestAssignedEventDto e) {
    log.info(
            "Уведомление создателя о назначении: requestId={}, userId={}",
            e.getRequestId(),
            e.getCreatedByUserId()
    );

    Notification notification = Notification.builder()
            .userId(e.getCreatedByUserId())
            .requestId(e.getRequestId())
            .type(NotificationType.ASSIGNED)
            .title("Назначение заявки!")
            .message("Созданная вами заявка была назначена исполнителю: " + e.getTitle())
            .isRead(false)
            .createdAt(LocalDateTime.now())
            .build();

    notificationRepository.save(notification);
    outboxService.save(notification);
  }

  @Override
  @Transactional
  public void notifyExecutor(RequestAssignedEventDto e) {
    log.info(
            "Уведомление о назначении: requestId={}, userId={}",
            e.getRequestId(),
            e.getAssignedUserId()
    );

    Notification notification = Notification.builder()
            .userId(e.getAssignedUserId())
            .requestId(e.getRequestId())
            .type(NotificationType.ASSIGNED)
            .title("Новая заявка!")
            .message("Вам назначена заявка: " + e.getTitle())
            .isRead(false)
            .createdAt(LocalDateTime.now())
            .build();

    notificationRepository.save(notification);
    outboxService.save(notification);
  }

  @Override
  @Transactional
  public void notifyStatusChanged(UUID userId, RequestStatusChangedEventDto e) {
    log.info(
            "Уведомление об изменении статуса: requestId={}, status={}",
            e.getRequestId(),
            e.getStatus()
    );

    Notification notification = Notification.builder()
            .userId(userId)
            .requestId(e.getRequestId())
            .type(NotificationType.STATUS_CHANGED)
            .title("Статус заявки изменен")
            .message("Статус изменен на: " + e.getStatus())
            .isRead(false)
            .createdAt(LocalDateTime.now())
            .build();

    notificationRepository.save(notification);
    outboxService.save(notification);
  }

  @Override
  @Transactional
  public void notifySlaWarning(RequestSlaWarningEventDto e) {
    log.info(
            "SLA warning: requestId={}, minutesLeft={}",
            e.getRequestId(),
            e.getMinutesLeft()
    );

    Notification notification = Notification.builder()
            .userId(e.getUserId())
            .requestId(e.getRequestId())
            .type(NotificationType.SLA_WARNING)
            .title("Приближается дедлайн")
            .message(buildSlaWarningMessage(e))
            .isRead(false)
            .createdAt(LocalDateTime.now())
            .build();

    notificationRepository.save(notification);
    outboxService.save(notification);
  }

  @Override
  @Transactional
  public void notifySlaViolation(RequestSlaViolationEventDto e) {
    log.info("SLA violation: requestId={}", e.getRequestId());

    Notification notification = Notification.builder()
            .userId(e.getUserId())
            .requestId(e.getRequestId())
            .type(NotificationType.SLA_VIOLATION)
            .title("Приближается дедлайн")
            .message("Срок выполнения заявки \"" + e.getTitle() + "\" нарушен!")
            .isRead(false)
            .createdAt(LocalDateTime.now())
            .build();

    notificationRepository.save(notification);
    outboxService.save(notification);
  }

  @Override
  public List<NotificationDto> getUserNotifications(UUID userId) {
    log.debug("Получение уведомлений пользователя: userId={}", userId);

    if (userId == null) {
      log.warn("Ошибка получения уведомлений: userId=null");
      throw new IllegalArgumentException("Идентификатор пользователя обязателен");
    }
    return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
            .map(notificationMapper::toDto)
            .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void markNotificationAsRead(UUID id) {
    log.info("Отметка уведомления как прочитанного: id={}", id);

    Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> {
              log.warn("Уведомление не найдено: id={}", id);
              return new NotFoundException("Уведомление не найдено");
            });
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
