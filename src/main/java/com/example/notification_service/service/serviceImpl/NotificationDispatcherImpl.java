package com.example.notification_service.service.serviceImpl;

import com.example.notification_service.entity.Notification;
import com.example.notification_service.entity.NotificationChannel;
import com.example.notification_service.repository.NotificationChannelRepository;
import com.example.notification_service.service.NotificationDispatcher;
import com.example.notification_service.service.sender.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationDispatcherImpl implements NotificationDispatcher {

  private final ChannelServiceImpl channelService;
  private final List<NotificationSender> notificationSenders;
  private final NotificationChannelRepository notificationChannelRepository;

  @Override
  public void dispatch(Notification notification) {
    List<NotificationChannel> channels = channelService.getUserChannels(notification.getUserId());

    log.debug(
            "Отправка уведомления: userId={}, channels={}",
            notification.getUserId(),
            channels.size()
    );

    sendToChannels(channels, notification);
  }

  @Override
  public void dispatchCritical(Notification notification) {
    List<NotificationChannel> channels = notificationChannelRepository.findAllByOrderByCodeAsc();
    sendToChannels(channels, notification);
  }

  private void sendToChannels(List<NotificationChannel> channels, Notification notification) {
    for (NotificationChannel channel : channels) {
      notificationSenders.stream()
              .filter(s -> s.getChannelCode().equals(channel.getCode()))
              .findFirst()
              .ifPresent(s -> s.send(notification));
    }
  }
}
