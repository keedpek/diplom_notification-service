package com.example.notification_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification_delivery_log")
public class DeliveryLog {

  @Id
  private UUID notificationId;

  private LocalDateTime deliveredAt;
}
