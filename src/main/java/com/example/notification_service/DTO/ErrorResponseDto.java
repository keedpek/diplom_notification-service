package com.example.notification_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ErrorResponseDto {
  private int status;
  private String error;
  private String message;
  private String path;
}

