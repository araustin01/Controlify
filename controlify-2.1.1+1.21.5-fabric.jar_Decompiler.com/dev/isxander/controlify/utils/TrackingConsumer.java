package dev.isxander.controlify.utils;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public record TrackingConsumer(Consumer<Long> start, BiConsumer<Long, Long> progressConsumer, Consumer<Optional<Throwable>> onComplete) {
   public TrackingConsumer(Consumer<Long> start, BiConsumer<Long, Long> progressConsumer, Consumer<Optional<Throwable>> onComplete) {
      this.start = start;
      this.progressConsumer = progressConsumer;
      this.onComplete = onComplete;
   }

   public Consumer<Long> start() {
      return this.start;
   }

   public BiConsumer<Long, Long> progressConsumer() {
      return this.progressConsumer;
   }

   public Consumer<Optional<Throwable>> onComplete() {
      return this.onComplete;
   }
}
