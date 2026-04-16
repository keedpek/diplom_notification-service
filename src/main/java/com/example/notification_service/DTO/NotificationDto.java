package com.example.notification_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class NotificationDto {
  private UUID id;
  private String type;
  private String title;
  private String message;
  private Boolean isRead;
  private LocalDateTime createdAt;
}
