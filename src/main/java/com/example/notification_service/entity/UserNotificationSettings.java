package com.example.notification_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
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
