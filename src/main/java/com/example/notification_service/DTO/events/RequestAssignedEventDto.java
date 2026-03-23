package com.example.notification_service.DTO.events;

import lombok.Data;

import java.util.UUID;

@Data
public class RequestAssignedEventDto {
  private UUID requestId;
  private UUID assignedUserId;
  private String title;
}
