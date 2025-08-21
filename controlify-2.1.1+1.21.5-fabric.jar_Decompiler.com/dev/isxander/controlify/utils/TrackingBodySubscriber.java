package dev.isxander.controlify.utils;

import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodySubscriber;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Flow.Subscription;

public class TrackingBodySubscriber<T> implements BodySubscriber<T> {
   private final BodySubscriber<T> delegate;
   private final TrackingConsumer consumer;
   private long receivedBytes;
   private final long contentLengthIfKnown;

   public TrackingBodySubscriber(BodySubscriber<T> delegate, TrackingConsumer consumer, long contentLengthIfKnown) {
      this.delegate = delegate;
      this.consumer = consumer;
      this.contentLengthIfKnown = contentLengthIfKnown;
   }

   public CompletionStage<T> getBody() {
      return this.delegate.getBody();
   }

   public void onSubscribe(Subscription subscription) {
      this.consumer.start().accept(this.contentLengthIfKnown);
      this.delegate.onSubscribe(subscription);
   }

   public void onNext(List<ByteBuffer> item) {
      this.receivedBytes += this.countBytes(item);
      this.delegate.onNext(item);
      this.consumer.progressConsumer().accept(this.receivedBytes, this.contentLengthIfKnown);
   }

   public void onError(Throwable throwable) {
      this.consumer.onComplete().accept(Optional.of(throwable));
      this.delegate.onError(throwable);
   }

   public void onComplete() {
      this.consumer.onComplete().accept(Optional.empty());
      this.delegate.onComplete();
   }

   private long countBytes(List<ByteBuffer> buffers) {
      return buffers.stream().mapToLong(Buffer::remaining).sum();
   }

   public static <T> BodyHandler<T> bodyHandler(BodyHandler<T> delegate, TrackingConsumer consumer) {
      return (responseInfo) -> {
         return new TrackingBodySubscriber(delegate.apply(responseInfo), consumer, responseInfo.headers().firstValueAsLong("Content-Length").orElse(-1L));
      };
   }
}
