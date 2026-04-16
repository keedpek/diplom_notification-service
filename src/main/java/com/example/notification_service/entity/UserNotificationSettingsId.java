package com.example.notification_service.entity;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserNotificationSettingsId implements Serializable {
  private UUID userId;
  private Short channelId;
}
