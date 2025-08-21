package dev.isxander.controlify.bindings.output;

import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.bindings.StateAccess;

public class JustTappedOutput implements DigitalOutput {
   public static final int DEFAULT_MAX_HOLD_TIME_TICKS = 2;
   private final StateAccess stateAccess;
   private final int maxHoldTime;

   public JustTappedOutput(InputBinding binding, int maxHoldTimeTicks) {
      this.stateAccess = binding.createStateAccess(maxHoldTimeTicks + 2);
      this.maxHoldTime = maxHoldTimeTicks;
   }

   public JustTappedOutput(InputBinding binding) {
      this(binding, 2);
   }

   public boolean get() {
      if (this.stateAccess.isSuppressed()) {
         return false;
      } else {
         boolean justReleased = this.stateAccess.digital(0);
         if (!justReleased) {
            return false;
         } else {
            for(int i = 1; i < this.maxHoldTime + 2; ++i) {
               boolean state = this.stateAccess.digital(i);
               if (!state) {
                  return true;
               }
            }

            return false;
         }
      }
   }
}
