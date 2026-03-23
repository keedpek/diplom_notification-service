package com.example.notification_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "user_notification_settings")
@Getter
@Setter
@IdClass(UserNotificationSettingsId.class)
public class UserNotificationSettings {
  @Id
  private UUID userId;

  @Id
  private Short channelId;

  @Column(nullable = false)
  private Boolean enabled;
}
