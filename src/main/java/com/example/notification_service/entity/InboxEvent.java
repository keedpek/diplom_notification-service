package com.example.notification_service.entity;

import com.example.notification_service.enums.EventType;
import com.example.notification_service.enums.NotificationInboxStatus;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification_inbox")
public class InboxEvent {

  @Id
  @Column(nullable = false, unique = true)
  private UUID eventId;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private EventType eventType;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb", nullable = false)
  private JsonNode payload;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private NotificationInboxStatus status;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  private LocalDateTime processedAt;
}
