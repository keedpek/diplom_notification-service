package com.example.notification_service.service.sender;

import com.example.notification_service.entity.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebSender implements NotificationSender {

  private final SimpMessagingTemplate template;

  @Override
  public String getChannelCode() {
    return "WEB";
  }

  @Override
  public void send(Notification notification) {
    log.debug("Отправка WebSocket уведомления: userId={}", notification.getUserId());
    template.convertAndSend("/topic/notifications/" + notification.getUserId(), notification);
  }
}
