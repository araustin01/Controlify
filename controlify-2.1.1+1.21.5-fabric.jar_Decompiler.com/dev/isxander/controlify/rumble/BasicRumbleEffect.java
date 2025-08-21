package dev.isxander.controlify.rumble;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import net.minecraft.class_310;
import net.minecraft.class_437;
import org.apache.commons.lang3.Validate;

public final class BasicRumbleEffect implements RumbleEffect {
   private final RumbleState[] keyframes;
   private int tick = 0;
   private boolean finished;
   private int priority = 0;
   private BooleanSupplier earlyFinishCondition = () -> {
      return false;
   };

   public BasicRumbleEffect(RumbleState[] keyframes) {
      this.keyframes = keyframes;
   }

   public void tick() {
      ++this.tick;
      if (this.tick >= this.keyframes.length || this.earlyFinishCondition.getAsBoolean()) {
         this.finished = true;
      }

   }

   public RumbleState currentState() {
      if (this.tick == 0) {
         throw new IllegalStateException("Effect hasn't ticked yet.");
      } else {
         return this.keyframes[this.tick - 1];
      }
   }

   public int age() {
      return this.tick;
   }

   public boolean isFinished() {
      return this.finished;
   }

   public int priority() {
      return this.priority;
   }

   public BasicRumbleEffect prioritised(int priority) {
      this.priority = priority;
      return this;
   }

   public RumbleState[] states() {
      return this.keyframes;
   }

   public BasicRumbleEffect earlyFinish(BooleanSupplier condition) {
      BooleanSupplier current = this.earlyFinishCondition;
      this.earlyFinishCondition = () -> {
         return current.getAsBoolean() || condition.getAsBoolean();
      };
      return this;
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (obj != null && obj.getClass() == this.getClass()) {
         BasicRumbleEffect that = (BasicRumbleEffect)obj;
         return Arrays.equals(this.states(), that.states()) && this.priority() == that.priority();
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{Arrays.hashCode(this.states()), this.priority()});
   }

   public String toString() {
      String var10000 = Arrays.toString(this.states());
      return "RumbleEffect[states=" + var10000 + ",priority=" + this.priority() + "]";
   }

   public BasicRumbleEffect join(BasicRumbleEffect other) {
      return join(this, other);
   }

   public BasicRumbleEffect repeat(int count) {
      Validate.isTrue(count > 0, "count must be greater than 0", new Object[0]);
      if (count == 1) {
         return this;
      } else {
         BasicRumbleEffect effect = this;

         for(int i = 0; i < count - 1; ++i) {
            effect = join(effect, this);
         }

         return effect;
      }
   }

   public static BasicRumbleEffect byTick(Function<Integer, RumbleState> stateFunction, int durationTicks) {
      RumbleState[] states = new RumbleState[durationTicks];

      for(int i = 0; i < durationTicks; ++i) {
         states[i] = (RumbleState)stateFunction.apply(i);
      }

      return new BasicRumbleEffect(states);
   }

   public static BasicRumbleEffect byTime(Function<Float, RumbleState> stateFunction, int durationTicks) {
      return byTick((tick) -> {
         return (RumbleState)stateFunction.apply((float)tick / (float)durationTicks);
      }, durationTicks);
   }

   public static BasicRumbleEffect constant(float strong, float weak, int durationTicks) {
      return byTick((tick) -> {
         return new RumbleState(strong, weak);
      }, durationTicks);
   }

   public static BasicRumbleEffect empty(int durationTicks) {
      return byTick((tick) -> {
         return new RumbleState(0.0F, 0.0F);
      }, durationTicks);
   }

   public static BasicRumbleEffect join(BasicRumbleEffect... effects) {
      int totalTicks = 0;
      BasicRumbleEffect[] var2 = effects;
      int currentTick = effects.length;

      for(int var4 = 0; var4 < currentTick; ++var4) {
         BasicRumbleEffect effect = var2[var4];
         totalTicks += effect.states().length;
      }

      RumbleState[] states = new RumbleState[totalTicks];
      currentTick = 0;
      BasicRumbleEffect[] var13 = effects;
      int var14 = effects.length;

      for(int var6 = 0; var6 < var14; ++var6) {
         BasicRumbleEffect effect = var13[var6];
         RumbleState[] var8 = effect.states();
         int var9 = var8.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            RumbleState state = var8[var10];
            states[currentTick] = state;
            ++currentTick;
         }
      }

      return new BasicRumbleEffect(states);
   }

   public static BooleanSupplier finishOnScreenChange() {
      class_437 screen = class_310.method_1551().field_1755;
      return () -> {
         return screen != class_310.method_1551().field_1755;
      };
   }
}
