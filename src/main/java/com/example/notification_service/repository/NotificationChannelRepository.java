package com.example.notification_service.repository;

import com.example.notification_service.entity.NotificationChannel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationChannelRepository extends JpaRepository<NotificationChannel, Short> {
  Optional<NotificationChannel> findByCode(String code);
  List<NotificationChannel> findAllByOrderByCodeAsc();
}
