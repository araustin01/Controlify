package dev.isxander.controlify.utils.animation.impl;

import dev.isxander.controlify.utils.animation.api.Animation;
import dev.isxander.controlify.utils.animation.api.EasingFunction;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AnimationImpl implements Animation {
   private final List<AnimationImpl.AnimationConsumer> consumers;
   private final List<AnimationImpl.AnimationDeltaConsumer> deltaConsumers;
   private EasingFunction easing;
   private float time;
   private int duration;
   private boolean done;

   public AnimationImpl() {
      this.easing = EasingFunction.LINEAR;
      this.consumers = new ObjectArrayList();
      this.deltaConsumers = new ObjectArrayList();
   }

   public AnimationImpl(AnimationImpl other) {
      this.easing = EasingFunction.LINEAR;
      this.consumers = new ObjectArrayList(other.consumers);
      this.deltaConsumers = new ObjectArrayList(other.deltaConsumers);
      this.easing = other.easing;
      this.time = other.time;
      this.duration = other.duration;
      this.done = other.done;
   }

   public Animation consumerI(Consumer<Integer> consumer, double start, double end) {
      this.consumers.add(new AnimationImpl.AnimationConsumer((d) -> {
         consumer.accept((int)d);
      }, start, end));
      return this;
   }

   public Animation consumerF(Consumer<Float> consumer, double start, double end) {
      this.consumers.add(new AnimationImpl.AnimationConsumer((d) -> {
         consumer.accept((float)d);
      }, start, end));
      return this;
   }

   public Animation consumerD(Consumer<Double> consumer, double start, double end) {
      this.consumers.add(new AnimationImpl.AnimationConsumer(consumer, start, end));
      return this;
   }

   public Animation deltaConsumerI(Consumer<Integer> consumer, double start, double end) {
      this.deltaConsumers.add(new AnimationImpl.AnimationDeltaConsumer((d) -> {
         consumer.accept((int)d);
      }, start, end));
      return this;
   }

   public Animation deltaConsumerF(Consumer<Float> consumer, double start, double end) {
      this.deltaConsumers.add(new AnimationImpl.AnimationDeltaConsumer((d) -> {
         consumer.accept((float)d);
      }, start, end));
      return this;
   }

   public Animation deltaConsumerD(Consumer<Double> consumer, double start, double end) {
      this.deltaConsumers.add(new AnimationImpl.AnimationDeltaConsumer(consumer, start, end));
      return this;
   }

   public Animation duration(int ticks) {
      this.duration = ticks;
      return this;
   }

   public Animation easing(EasingFunction easing) {
      this.easing = easing;
      return this;
   }

   public Animation copy() {
      return new AnimationImpl(this);
   }

   public void tick(float tickDelta) {
      if (this.duration <= 0 || this.time >= (float)this.duration) {
         this.done = true;
      }

      if (!this.done) {
         this.time += tickDelta;
         this.updateConsumers();
      }
   }

   private void updateConsumers() {
      float progress = this.easing.ease(this.time / (float)this.duration);
      this.consumers.forEach((consumer) -> {
         consumer.tick(progress);
      });
      this.deltaConsumers.forEach((consumer) -> {
         consumer.tick(progress);
      });
   }

   public void skipToEnd() {
      this.time = (float)this.duration;
      this.updateConsumers();
      this.done = true;
   }

   public void abort() {
      this.done = true;
   }

   public Animation play() {
      Animator.INSTANCE.add(this);
      return this;
   }

   public boolean hasStarted() {
      return this.time > 0.0F;
   }

   public boolean isDone() {
      return this.done;
   }

   public boolean isPlaying() {
      return this.hasStarted() && !this.isDone();
   }

   private static record AnimationConsumer(Consumer<Double> consumer, double start, double end) {
      private AnimationConsumer(Consumer<Double> consumer, double start, double end) {
         this.consumer = consumer;
         this.start = start;
         this.end = end;
      }

      public void tick(float tickDelta) {
         this.consumer.accept(this.start + (this.end - this.start) * (double)tickDelta);
      }

      public Consumer<Double> consumer() {
         return this.consumer;
      }

      public double start() {
         return this.start;
      }

      public double end() {
         return this.end;
      }
   }

   private static class AnimationDeltaConsumer {
      private final Consumer<Double> consumer;
      private final double start;
      private final double end;
      private double lastValue;

      public AnimationDeltaConsumer(Consumer<Double> consumer, double start, double end) {
         this.consumer = consumer;
         this.start = start;
         this.end = end;
      }

      public void tick(float tickDelta) {
         double value = this.start + (this.end - this.start) * (double)tickDelta;
         this.consumer.accept(value - this.lastValue);
         this.lastValue = value;
      }
   }
}
