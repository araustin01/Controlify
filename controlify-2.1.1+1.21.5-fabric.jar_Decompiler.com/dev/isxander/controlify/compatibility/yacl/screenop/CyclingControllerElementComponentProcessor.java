package dev.isxander.controlify.compatibility.yacl.screenop;

import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.compatibility.yacl.mixins.ControllerWidgetAccessor;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.yacl3.gui.controllers.cycling.CyclingControllerElement;

public class CyclingControllerElementComponentProcessor implements ComponentProcessor {
   private final CyclingControllerElement cyclingController;
   private boolean prevLeft;
   private boolean prevRight;

   public CyclingControllerElementComponentProcessor(CyclingControllerElement cyclingController) {
      this.cyclingController = cyclingController;
   }

   public boolean overrideControllerNavigation(ScreenProcessor<?> screen, ControllerEntity controller) {
      boolean left = ControlifyBindings.CYCLE_OPT_BACKWARD.on(controller).digitalNow();
      boolean right = ControlifyBindings.CYCLE_OPT_FORWARD.on(controller).digitalNow();
      if (!((ControllerWidgetAccessor)this.cyclingController).getControl().option().available()) {
         return false;
      } else if (left && !this.prevLeft) {
         this.prevLeft = true;
         this.prevRight = false;
         this.cyclingController.cycleValue(-1);
         return true;
      } else if (right && !this.prevRight) {
         this.prevLeft = false;
         this.prevRight = true;
         this.cyclingController.cycleValue(1);
         return true;
      } else {
         this.prevLeft = left;
         this.prevRight = right;
         return false;
      }
   }
}
