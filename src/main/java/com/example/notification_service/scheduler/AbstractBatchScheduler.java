package com.example.notification_service.scheduler;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Slf4j
public abstract class AbstractBatchScheduler<T> {

  private final ExecutorService executor;
  private final String schedulerName;

  protected AbstractBatchScheduler(int threadPoolSize, String schedulerName) {
    this.executor = Executors.newFixedThreadPool(threadPoolSize);
    this.schedulerName = schedulerName;
  }

  protected void processBatch(List<T> batch, Consumer<T> processor) {
    log.info("[{}] Обработка {} событий(-ия)", schedulerName, batch.size());

    List<CompletableFuture<Void>> futures = batch.stream()
            .map(item -> CompletableFuture.runAsync(() -> processor.accept(item), executor))
            .toList();

    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
  }
}
