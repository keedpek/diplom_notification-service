package com.example.notification_service.DTO.events;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class RequestAssignedEventDto {

  @NotNull(message = "Идентификатор заявки обязателен")
  private UUID requestId;

  @NotNull(message = "Идентификатор исполнителя обязателен")
  private UUID assignedUserId;

  @NotBlank(message = "Заголовок заявки обязателен")
  @Size(max = 500, message = "Заголовок не длиннее 500 символов")
  private String title;
}
