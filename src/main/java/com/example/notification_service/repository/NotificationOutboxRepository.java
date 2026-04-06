package com.example.notification_service.repository;

import com.example.notification_service.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface NotificationOutboxRepository extends JpaRepository<OutboxEvent, UUID> {

  @Modifying
  @Query(value = """
    UPDATE notification_outbox o
    SET status = 'PROCESSING',
        locked = true
    WHERE id IN (
        SELECT id FROM notification_outbox
        WHERE status = 'NEW'
            AND (next_retry_at IS NULL OR next_retry_at <= now())
            AND locked = false
        ORDER BY created_at
        LIMIT 50
        FOR UPDATE SKIP LOCKED
    )
    RETURNING *
  """, nativeQuery = true)
  List<OutboxEvent> lockBatch();
}
