package com.example.notification_service.DTO.events;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RequestSlaWarningEventDto {
  private UUID requestId;

  private UUID userId;

  private String title;

  private Integer minutesLeft;

  private LocalDateTime deadline;
}
