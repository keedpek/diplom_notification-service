package com.example.notification_service.service.sender;

import com.example.notification_service.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSender implements NotificationSender {

  @Override
  public String getChannelCode() {
    return "EMAIL";
  }

  @Override
  public void send(Notification notification) {
    //TODO
  }
}
