package com.example.notification_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseDto {
  private int status;
  private String error;
  private String message;
  private String path;
}

