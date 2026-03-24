package com.example.notification_service.DTO.events;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class RequestStatusChangedEventDto {

  @NotNull(message = "Идентификатор заявки обязателен")
  private UUID requestId;

  @NotNull(message = "Идентификатор пользователя обязателен")
  private UUID userId;

  @NotBlank(message = "Статус обязателен") 
  @Pattern(
    regexp = "^(NEW|ASSIGNED|IN_PROGRESS|WAITING_FOR_RESPONSE|COMPLETED|CANCELLED)$",
    message = "Некорректный статус заявки"
  )
  private String status;
}
