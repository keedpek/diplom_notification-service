package com.example.notification_service.repository;

import com.example.notification_service.entity.DeliveryLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationDeliveryLogRepository extends JpaRepository<DeliveryLog, UUID> {
}
