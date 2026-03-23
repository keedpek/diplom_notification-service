package com.example.notification_service.DTO.events;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RequestSlaViolationEventDto {
  private UUID requestId;

  private UUID userId;

  private String title;

  private LocalDateTime deadline;

  private LocalDateTime violatedAt;
}
