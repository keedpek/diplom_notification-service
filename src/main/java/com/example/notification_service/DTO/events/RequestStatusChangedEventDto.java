package com.example.notification_service.DTO.events;

import lombok.Data;

import java.util.UUID;

@Data
public class RequestStatusChangedEventDto {
  private UUID requestId;
  private UUID userId;
  private String status;
}
