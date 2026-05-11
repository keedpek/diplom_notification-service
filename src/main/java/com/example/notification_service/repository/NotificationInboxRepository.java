package com.example.notification_service.repository;

import com.example.notification_service.entity.InboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface NotificationInboxRepository extends JpaRepository<InboxEvent, UUID> {

  @Modifying
  @Query(value = """
    UPDATE notification_inbox i
    SET status = 'PROCESSING'
    WHERE event_id IN (
        SELECT event_id FROM notification_inbox
        WHERE status IN ('NEW', 'ERROR')
        ORDER BY created_at
        LIMIT :limit
        FOR UPDATE SKIP LOCKED
    )
    RETURNING *
  """, nativeQuery = true)
  List<InboxEvent> lockBatchForProcessing(@Param("limit") int limit);

  @Modifying
  @Query("""
    UPDATE InboxEvent i
    SET i.status = com.example.notification_service.enums.NotificationInboxStatus.SUCCESS,
        i.processedAt = :processedAt
    WHERE i.eventId = :eventId
  """)
  void markSuccess(@Param("eventId") UUID eventId, @Param("processedAt") LocalDateTime processedAt);

  @Modifying
  @Query("""
    UPDATE InboxEvent i
    SET i.status = com.example.notification_service.enums.NotificationInboxStatus.ERROR
    WHERE i.eventId = :eventId
  """)
  void markError(@Param("eventId") UUID eventId);
}
