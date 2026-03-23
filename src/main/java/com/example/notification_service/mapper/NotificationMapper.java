package com.example.notification_service.mapper;

import com.example.notification_service.DTO.NotificationDto;
import com.example.notification_service.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

  @Mapping(target = "type", expression = "java(notification.getType().name())")
  NotificationDto toDto(Notification notification);
}
