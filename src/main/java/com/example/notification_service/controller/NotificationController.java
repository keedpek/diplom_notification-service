package com.example.notification_service.controller;

import com.example.notification_service.DTO.NotificationDto;
import com.example.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  @GetMapping()
  public List<NotificationDto> getUserNotifications(@RequestParam UUID userId) {
    return notificationService.getUserNotifications(userId);
  }

  @PostMapping("{id}/read")
  public void readNotification(@PathVariable UUID id) {
    notificationService.markNotificationAsRead(id);
  }
}
