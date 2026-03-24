package com.example.notification_service.controller;

//ИМИТАЦИЯ СОБЫТИЙ ИЗ КАФКИ

import com.example.notification_service.DTO.events.RequestAssignedEventDto;
import com.example.notification_service.DTO.events.RequestSlaViolationEventDto;
import com.example.notification_service.DTO.events.RequestSlaWarningEventDto;
import com.example.notification_service.DTO.events.RequestStatusChangedEventDto;
import com.example.notification_service.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/events")
@RequiredArgsConstructor
public class EventController {

  private final NotificationService notificationService;

  @PostMapping("/assigned")
  public void assigned(@Valid @RequestBody RequestAssignedEventDto e) {
    notificationService.notifyExecutor(e);
  }

  @PostMapping("/status-changed")
  public void statusChanged(@Valid @RequestBody RequestStatusChangedEventDto e) {
    notificationService.notifyStatusChanged(e);
  }

  @PostMapping("/sla-warning")
  public void slaWarning(@Valid @RequestBody RequestSlaWarningEventDto e) {
    notificationService.notifySlaWarning(e);
  }

  @PostMapping("/sla-violation")
  public void slaViolation(@Valid @RequestBody RequestSlaViolationEventDto e) {
    notificationService.notifySlaViolation(e);
  }
}
