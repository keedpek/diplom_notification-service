package com.example.notification_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserNotificationSettingsId.class)
@Table(name = "user_notification_settings")
public class UserNotificationSettings {
  @Id
  private UUID userId;

  @Id
  private Short channelId;

  @Column(nullable = false)
  private Boolean enabled;
}
