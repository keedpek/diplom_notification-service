package com.example.notification_service.service.serviceImpl;

import com.example.notification_service.entity.NotificationChannel;
import com.example.notification_service.entity.UserNotificationSettings;
import com.example.notification_service.repository.NotificationChannelRepository;
import com.example.notification_service.repository.UserNotificationSettingsRepository;
import com.example.notification_service.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {

  private final UserNotificationSettingsRepository settingsRepository;
  private final NotificationChannelRepository channelRepository;

  @Override
  public List<NotificationChannel> getUserChannels(UUID userId) {
    log.debug("Получение каналов пользователя: userId={}", userId);

    List<UserNotificationSettings> channels = settingsRepository.findByUserId(userId);

    return channels.stream()
            .filter(UserNotificationSettings::getEnabled)
            .map(s -> channelRepository.findById(s.getChannelId())
                    .orElse(null)
            )
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
  }
}
