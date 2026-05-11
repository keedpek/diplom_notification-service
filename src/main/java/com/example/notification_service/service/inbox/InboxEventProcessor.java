package com.example.notification_service.service.inbox;

import com.example.notification_service.entity.InboxEvent;

public interface InboxEventProcessor {
  void process(InboxEvent event);
}
