package dev.isxander.controlify.screenop.compat;

import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.utils.HoldRepeatHelper;

public abstract class AbstractSliderComponentProcessor implements ComponentProcessor {
   private final HoldRepeatHelper holdRepeatHelper = new HoldRepeatHelper(15, 3);

   public boolean overrideControllerNavigation(ScreenProcessor<?> screen, ControllerEntity controller) {
      boolean left = ControlifyBindings.CYCLE_OPT_BACKWARD.on(controller).digitalNow();
      boolean leftPrev = ControlifyBindings.CYCLE_OPT_BACKWARD.on(controller).digitalPrev();
      boolean right = ControlifyBindings.CYCLE_OPT_FORWARD.on(controller).digitalNow();
      boolean rightPrev = ControlifyBindings.CYCLE_OPT_FORWARD.on(controller).digitalPrev();
      boolean repeatEventAvailable = this.holdRepeatHelper.canNavigate();
      if (!left || !repeatEventAvailable && leftPrev) {
         if (!right || !repeatEventAvailable && rightPrev) {
            return false;
         }

         this.incrementSlider(false);
         if (!rightPrev) {
            this.holdRepeatHelper.reset();
         }
      } else {
         this.incrementSlider(true);
         if (!leftPrev) {
            this.holdRepeatHelper.reset();
         }
      }

      this.holdRepeatHelper.onNavigate();
      return true;
   }

   protected abstract void incrementSlider(boolean var1);
}
