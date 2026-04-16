package com.example.notification_service.repository;

import com.example.notification_service.entity.UserNotificationSettings;
import com.example.notification_service.entity.UserNotificationSettingsId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserNotificationSettingsRepository extends JpaRepository<UserNotificationSettings, UserNotificationSettingsId> {
  List<UserNotificationSettings> findByUserId(UUID userId);
}
