package com.example.notification_service.exceptions;

public class OutboxPersistenceException extends RuntimeException {
  public OutboxPersistenceException(String message) {
    super(message);
  }
}
