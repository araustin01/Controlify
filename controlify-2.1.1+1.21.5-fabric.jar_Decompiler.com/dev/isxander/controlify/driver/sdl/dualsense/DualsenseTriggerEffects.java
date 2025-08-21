package dev.isxander.controlify.driver.sdl.dualsense;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public final class DualsenseTriggerEffects {
   @Contract(
      pure = true
   )
   public static DS5EffectsState.TriggerEffect off() {
      return new DS5EffectsState.TriggerEffect(DualsenseTriggerEffects.EffectType.OFF, new byte[0]);
   }

   @Contract(
      pure = true
   )
   public static DS5EffectsState.TriggerEffect feedback(@Range(from = 0L,to = 9L) byte position, @Range(from = 0L,to = 8L) byte strength) {
      Validate.inclusiveBetween(0L, 9L, (long)position, "Position must be between 0 and 9 inclusive");
      Validate.inclusiveBetween(0L, 8L, (long)strength, "Strength must be between 0 and 8 inclusive");
      if (strength <= 0) {
         return off();
      } else {
         byte forceValue = (byte)(strength - 1 & 7);
         int forceZones = 0;
         char activeZones = 0;

         for(int i = position; i < 10; ++i) {
            forceZones |= forceValue << 3 * i;
            activeZones |= (char)(1 << i);
         }

         return new DS5EffectsState.TriggerEffect(DualsenseTriggerEffects.EffectType.FEEDBACK, new byte[]{(byte)(activeZones & 255), (byte)(activeZones >> 8 & 255), (byte)(forceZones & 255), (byte)(forceZones >> 8 & 255), (byte)(forceZones >> 16 & 255), (byte)(forceZones >> 24 & 255)});
      }
   }

   @Contract(
      pure = true
   )
   public static DS5EffectsState.TriggerEffect weapon(@Range(from = 2L,to = 7L) byte startPosition, @Range(from = 3L,to = 8L) byte endPosition, @Range(from = 0L,to = 8L) byte strength) {
      Validate.inclusiveBetween(2L, 7L, (long)startPosition, "Start position must be between 2 and 7 inclusive");
      Validate.inclusiveBetween((long)(startPosition + 1), 8L, (long)endPosition, "End position must be between start+1 and 8 inclusive");
      Validate.inclusiveBetween(0L, 8L, (long)strength, "Strength must be between 0 and 8 inclusive");
      Validate.isTrue(startPosition < endPosition, "Start position must be less than end position", new Object[0]);
      if (strength > 0) {
         char startAndStopZones = (char)(1 << startPosition | 1 << endPosition);
         return new DS5EffectsState.TriggerEffect(DualsenseTriggerEffects.EffectType.WEAPON, new byte[]{(byte)(startAndStopZones & 255), (byte)(startAndStopZones >> 8 & 255), (byte)(strength - 1)});
      } else {
         return off();
      }
   }

   @Contract(
      pure = true
   )
   public static DS5EffectsState.TriggerEffect vibration(@Range(from = 0L,to = 9L) byte position, @Range(from = 0L,to = 8L) byte amplitude, byte frequency) {
      Validate.inclusiveBetween(0L, 9L, (long)position, "Position must be between 0 and 9 inclusive");
      Validate.inclusiveBetween(0L, 8L, (long)amplitude, "Amplitude must be between 0 and 8 inclusive");
      if (amplitude > 0 && frequency > 0) {
         byte strengthValue = (byte)(amplitude - 1 & 7);
         int amplitudeZones = 0;
         char activeZones = 0;

         for(int i = position; i < 10; ++i) {
            amplitudeZones |= strengthValue << 3 * i;
            activeZones |= (char)(1 << i);
         }

         return new DS5EffectsState.TriggerEffect(DualsenseTriggerEffects.EffectType.VIBRATION, new byte[]{(byte)(activeZones & 255), (byte)(activeZones >> 8 & 255), (byte)(amplitudeZones & 255), (byte)(amplitudeZones >> 8 & 255), (byte)(amplitudeZones >> 16 & 255), (byte)(amplitudeZones >> 24 & 255), 0, 0, frequency});
      } else {
         return off();
      }
   }

   @Contract(
      pure = true
   )
   public static DS5EffectsState.TriggerEffect feedbackMultiplePosition(@NotNull byte[] strength) {
      Validate.notNull(strength, "Strength array must not be null", new Object[0]);
      Validate.isTrue(strength.length == 10, "Strength array must have 10 elements", new Object[0]);
      boolean allZero = true;

      int forceZones;
      for(forceZones = 0; forceZones < 10; ++forceZones) {
         allZero &= strength[forceZones] == 0;
         Validate.inclusiveBetween(0L, 8L, (long)strength[forceZones], "Strength i=%s must be between 0 and 8 inclusive".formatted(new Object[]{forceZones}));
      }

      if (!allZero) {
         forceZones = 0;
         char activeZones = 0;

         for(int i = 0; i < 10; ++i) {
            byte strengthValue = strength[i];
            if (strengthValue > 0) {
               byte forceValue = (byte)(strengthValue - 1 & 7);
               forceZones |= forceValue << 3 * i;
               activeZones |= (char)(1 << i);
            }
         }

         return new DS5EffectsState.TriggerEffect(DualsenseTriggerEffects.EffectType.FEEDBACK, new byte[]{(byte)(activeZones & 255), (byte)(activeZones >> 8 & 255), (byte)(forceZones & 255), (byte)(forceZones >> 8 & 255), (byte)(forceZones >> 16 & 255), (byte)(forceZones >> 24 & 255)});
      } else {
         return off();
      }
   }

   @Contract(
      pure = true
   )
   public static DS5EffectsState.TriggerEffect feedbackSlope(@Range(from = 0L,to = 8L) byte startPosition, @Range(from = 1L,to = 9L) byte endPosition, @Range(from = 1L,to = 8L) byte startStrength, @Range(from = 1L,to = 8L) byte endStrength) {
      Validate.inclusiveBetween(0L, 8L, (long)startPosition, "Start position must be between 0 and 8 inclusive");
      Validate.inclusiveBetween((long)(startPosition + 1), 9L, (long)endPosition, "End position must be between start+1 and 9 inclusive");
      Validate.inclusiveBetween(1L, 8L, (long)startStrength, "Start strength must be between 1 and 8 inclusive");
      Validate.inclusiveBetween(1L, 8L, (long)endStrength, "End strength must be between 1 and 8 inclusive");
      Validate.isTrue(startPosition < endPosition, "Start strength must be less than end position", new Object[0]);
      byte[] strength = new byte[10];
      float gradient = (float)(endStrength - startStrength) / (float)(endPosition - startPosition);

      for(int i = startPosition; i < 10; ++i) {
         strength[i] = i <= endPosition ? (byte)Math.round((float)startStrength + gradient * (float)(i - startPosition)) : endStrength;
      }

      return feedbackMultiplePosition(strength);
   }

   @Contract(
      pure = true
   )
   public static DS5EffectsState.TriggerEffect vibrationMultiplePosition(byte frequency, @NotNull byte[] amplitude) {
      Validate.notNull(amplitude, "Amplitude array must not be null", new Object[0]);
      Validate.isTrue(amplitude.length == 10, "Amplitude array must have 10 elements", new Object[0]);
      if (frequency > 0) {
         boolean allZero = true;

         int strengthZones;
         for(strengthZones = 0; strengthZones < 10; ++strengthZones) {
            allZero &= amplitude[strengthZones] == 0;
            Validate.inclusiveBetween(0L, 8L, (long)amplitude[strengthZones], "Amplitude i=%s must be between 0 and 8 inclusive".formatted(new Object[]{strengthZones}));
         }

         if (!allZero) {
            strengthZones = 0;
            char activeZones = 0;

            for(int i = 0; i < 10; ++i) {
               byte amplitudeValue = amplitude[i];
               if (amplitudeValue > 0) {
                  byte strengthValue = (byte)(amplitudeValue - 1 & 7);
                  strengthZones |= strengthValue << 3 * i;
                  activeZones |= (char)(1 << i);
               }
            }

            return new DS5EffectsState.TriggerEffect(DualsenseTriggerEffects.EffectType.VIBRATION, new byte[]{(byte)(activeZones & 255), (byte)(activeZones >> 8 & 255), (byte)(strengthZones & 255), (byte)(strengthZones >> 8 & 255), (byte)(strengthZones >> 16 & 255), (byte)(strengthZones >> 24 & 255), 0, 0, frequency});
         }
      }

      return off();
   }

   public static enum EffectType {
      OFF(5),
      FEEDBACK(33),
      WEAPON(37),
      VIBRATION(38),
      BOW(34),
      GALLOPING(35),
      MACHINE(39),
      SIMPLE_FEEDBACK(1),
      SIMPLE_WEAPON(2),
      SIMPLE_VIBRATION(3),
      LIMITED_FEEDBACK(17),
      LIMITED_WEAPON(18),
      DEBUG_FC(252),
      DEBUG_FD(253),
      DEBUG_FE(254);

      public final byte value;

      private EffectType(int value) {
         this.value = (byte)value;
      }

      // $FF: synthetic method
      private static DualsenseTriggerEffects.EffectType[] $values() {
         return new DualsenseTriggerEffects.EffectType[]{OFF, FEEDBACK, WEAPON, VIBRATION, BOW, GALLOPING, MACHINE, SIMPLE_FEEDBACK, SIMPLE_WEAPON, SIMPLE_VIBRATION, LIMITED_FEEDBACK, LIMITED_WEAPON, DEBUG_FC, DEBUG_FD, DEBUG_FE};
      }
   }
}
