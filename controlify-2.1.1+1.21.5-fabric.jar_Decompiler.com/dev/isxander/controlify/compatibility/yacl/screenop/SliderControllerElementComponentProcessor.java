package dev.isxander.controlify.compatibility.yacl.screenop;

import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.compatibility.yacl.mixins.ControllerWidgetAccessor;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.utils.HoldRepeatHelper;
import dev.isxander.yacl3.gui.controllers.slider.SliderControllerElement;

public class SliderControllerElementComponentProcessor implements ComponentProcessor {
   private final SliderControllerElement slider;
   private final HoldRepeatHelper holdRepeatHelper = new HoldRepeatHelper(15, 3);

   public SliderControllerElementComponentProcessor(SliderControllerElement element) {
      this.slider = element;
   }

   public boolean overrideControllerButtons(ScreenProcessor<?> screen, ControllerEntity controller) {
      boolean left = ControlifyBindings.CYCLE_OPT_BACKWARD.on(controller).digitalNow();
      boolean leftPrev = ControlifyBindings.CYCLE_OPT_BACKWARD.on(controller).digitalPrev();
      boolean right = ControlifyBindings.CYCLE_OPT_FORWARD.on(controller).digitalNow();
      boolean rightPrev = ControlifyBindings.CYCLE_OPT_FORWARD.on(controller).digitalPrev();
      if (!((ControllerWidgetAccessor)this.slider).getControl().option().available()) {
         return false;
      } else {
         boolean repeatEventAvailable = this.holdRepeatHelper.canNavigate();
         if (!left || !repeatEventAvailable && leftPrev) {
            if (!right || !repeatEventAvailable && rightPrev) {
               return false;
            }

            this.slider.incrementValue(1.0D);
            if (!rightPrev) {
               this.holdRepeatHelper.reset();
            }
         } else {
            this.slider.incrementValue(-1.0D);
            if (!leftPrev) {
               this.holdRepeatHelper.reset();
            }
         }

         this.holdRepeatHelper.onNavigate();
         return true;
      }
   }
}
