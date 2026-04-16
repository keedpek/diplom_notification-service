package com.example.notification_service.scheduler;

import com.example.notification_service.entity.OutboxEvent;
import com.example.notification_service.repository.NotificationOutboxRepository;
import com.example.notification_service.service.OutboxEventProcessor;
import com.example.notification_service.util.OutboxConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxScheduler {

  private final NotificationOutboxRepository notificationOutboxRepository;
  private final OutboxEventProcessor outboxEventProcessor;

  private final ExecutorService executorService = Executors.newFixedThreadPool(OutboxConstants.THREAD_POOL_SIZE);

  @Scheduled(fixedRate = OutboxConstants.SCHEDULER_INTERVAL)
  public void processEvents() {
    List<OutboxEvent> events = notificationOutboxRepository.lockBatch(OutboxConstants.BATCH_SIZE);
    log.info("Обработка {} событий(-ия)", events.size());

    List<CompletableFuture<Void>> futures = events.stream()
            .map(event -> CompletableFuture.runAsync(() -> outboxEventProcessor.process(event), executorService))
            .toList();

    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
  }
}
