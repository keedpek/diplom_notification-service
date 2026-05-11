package com.example.notification_service.service.inbox;

import com.example.notification_service.messaging.event.EventDto;
import com.example.notification_service.messaging.event.payload.EventDtoPayload;

public interface InboxService {
  void saveIfNew(EventDto<? extends EventDtoPayload> event);
}
