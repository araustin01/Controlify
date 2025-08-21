package dev.isxander.controlify.rumble;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import org.apache.commons.lang3.Validate;

public class ContinuousRumbleEffect implements RumbleEffect {
   private final Function<Integer, RumbleState> stateFunction;
   private final int priority;
   private final int timeout;
   private final int minTime;
   private int tick;
   private int age;
   private boolean stopped;
   private BooleanSupplier stopCondition;

   public ContinuousRumbleEffect(Function<Integer, RumbleState> stateFunction, int priority, int timeout, int minTime, BooleanSupplier stopCondition) {
      this.stateFunction = stateFunction;
      this.priority = priority;
      this.timeout = timeout;
      this.minTime = minTime;
      this.stopCondition = stopCondition;
   }

   public void tick() {
      ++this.tick;
      ++this.age;
      if (this.stopCondition.getAsBoolean()) {
         this.stop();
      }

   }

   public RumbleState currentState() {
      if (this.tick == 0) {
         throw new IllegalStateException("Effect hasn't ticked yet.");
      } else {
         return (RumbleState)this.stateFunction.apply(this.tick - 1);
      }
   }

   public void stop() {
      this.stopped = true;
   }

   public void heartbeat() {
      this.age = 0;
   }

   public int age() {
      return this.tick;
   }

   public boolean isFinished() {
      return (this.stopped || this.timeout > 0 && this.age >= this.timeout) && this.tick >= this.minTime;
   }

   public int priority() {
      return this.priority;
   }

   public static ContinuousRumbleEffect.Builder builder() {
      return new ContinuousRumbleEffect.Builder();
   }

   public static class Builder {
      private Function<Integer, RumbleState> stateFunction;
      private int priority;
      private int timeout = -1;
      private int minTime;
      private ContinuousRumbleEffect.Builder.InWorldProperties inWorldProperties;
      private BooleanSupplier stopCondition = () -> {
         return false;
      };

      private Builder() {
      }

      public ContinuousRumbleEffect.Builder byTick(Function<Integer, RumbleState> stateFunction) {
         this.stateFunction = stateFunction;
         return this;
      }

      public ContinuousRumbleEffect.Builder constant(RumbleState state) {
         this.stateFunction = (tick) -> {
            return state;
         };
         return this;
      }

      public ContinuousRumbleEffect.Builder constant(float strong, float weak) {
         return this.constant(new RumbleState(strong, weak));
      }

      public ContinuousRumbleEffect.Builder timeout(int timeoutTicks) {
         Validate.isTrue(timeoutTicks >= 0, "the timeout cannot be negative!", new Object[0]);
         this.timeout = timeoutTicks;
         return this;
      }

      public ContinuousRumbleEffect.Builder minTime(int minTimeTicks) {
         Validate.isTrue(minTimeTicks >= 0, "the minimum time cannot be negative!", new Object[0]);
         this.minTime = minTimeTicks;
         return this;
      }

      public ContinuousRumbleEffect.Builder priority(int priority) {
         this.priority = priority;
         return this;
      }

      public ContinuousRumbleEffect.Builder inWorld(Supplier<class_243> sourceLocation, float min, float max, float effectRange, Function<Float, Float> fallofFunction) {
         this.inWorldProperties = new ContinuousRumbleEffect.Builder.InWorldProperties(sourceLocation, min, max, effectRange, fallofFunction);
         this.stopCondition(() -> {
            return class_310.method_1551().field_1719 == null;
         });
         return this;
      }

      public ContinuousRumbleEffect.Builder stopCondition(BooleanSupplier stopCondition) {
         BooleanSupplier oldStopCondition = this.stopCondition;
         this.stopCondition = () -> {
            return stopCondition.getAsBoolean() || oldStopCondition.getAsBoolean();
         };
         return this;
      }

      public ContinuousRumbleEffect build() {
         Validate.notNull(this.stateFunction, "stateFunction cannot be null!", new Object[0]);
         Validate.isTrue(this.minTime <= this.timeout || this.timeout == -1, "the minimum time cannot be greater than the timeout!", new Object[0]);
         Function<Integer, RumbleState> stateFunction = this.stateFunction;
         if (this.inWorldProperties != null) {
            stateFunction = this.inWorldProperties.modify(stateFunction);
         }

         return new ContinuousRumbleEffect(stateFunction, this.priority, this.timeout, this.minTime, this.stopCondition);
      }

      private static record InWorldProperties(Supplier<class_243> sourceLocation, float minMagnitude, float maxMagnitude, float effectRange, Function<Float, Float> fallofFunction) {
         private InWorldProperties(Supplier<class_243> sourceLocation, float minMagnitude, float maxMagnitude, float effectRange, Function<Float, Float> fallofFunction) {
            this.sourceLocation = sourceLocation;
            this.minMagnitude = minMagnitude;
            this.maxMagnitude = maxMagnitude;
            this.effectRange = effectRange;
            this.fallofFunction = fallofFunction;
         }

         private Function<Integer, RumbleState> modify(Function<Integer, RumbleState> stateFunction) {
            return (tick) -> {
               if (class_310.method_1551().field_1719 == null) {
                  return RumbleState.NONE;
               } else {
                  float distanceSqr = (float)class_310.method_1551().field_1719.method_5707((class_243)this.sourceLocation.get());
                  float normalizedDistance = class_3532.method_15363(distanceSqr / (this.effectRange * this.effectRange), 0.0F, 1.0F);
                  float multiplier = class_3532.method_16439((Float)this.fallofFunction.apply(1.0F - normalizedDistance), this.minMagnitude, this.maxMagnitude);
                  return ((RumbleState)stateFunction.apply(tick)).mul(multiplier);
               }
            };
         }

         public Supplier<class_243> sourceLocation() {
            return this.sourceLocation;
         }

         public float minMagnitude() {
            return this.minMagnitude;
         }

         public float maxMagnitude() {
            return this.maxMagnitude;
         }

         public float effectRange() {
            return this.effectRange;
         }

         public Function<Float, Float> fallofFunction() {
            return this.fallofFunction;
         }
      }
   }
}
