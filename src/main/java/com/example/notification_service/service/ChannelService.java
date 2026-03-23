package com.example.notification_service.service;

import com.example.notification_service.entity.NotificationChannel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
  List<NotificationChannel> getUserChannels(UUID userId);
}
