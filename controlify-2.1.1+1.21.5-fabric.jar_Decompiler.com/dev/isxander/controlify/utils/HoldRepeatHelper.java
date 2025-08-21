package dev.isxander.controlify.utils;

import dev.isxander.controlify.api.bind.InputBinding;

public class HoldRepeatHelper {
   private final int initialDelay;
   private final int repeatDelay;
   private int currentDelay;
   private boolean hasResetThisTick = false;

   public HoldRepeatHelper(int initialDelay, int repeatDelay) {
      this.initialDelay = initialDelay;
      this.repeatDelay = repeatDelay;
      this.currentDelay = 0;
   }

   public boolean canNavigate() {
      return --this.currentDelay <= 0;
   }

   public void reset() {
      this.currentDelay = this.initialDelay;
      this.hasResetThisTick = true;
   }

   public void clearDelay() {
      this.currentDelay = 0;
   }

   public void onNavigate() {
      if (!this.hasResetThisTick) {
         this.currentDelay = this.repeatDelay;
      } else {
         this.hasResetThisTick = false;
      }

   }

   public boolean shouldAction(InputBinding binding) {
      boolean shouldAction = binding.digitalNow() && (this.canNavigate() || !binding.digitalPrev());
      if (shouldAction && !binding.digitalPrev()) {
         this.reset();
      }

      return shouldAction;
   }
}
