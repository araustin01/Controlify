package dev.isxander.controlify.compatibility.sodium.screenop;

import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ScreenProcessor;

public class TickBoxControlProcessor implements ComponentProcessor {
   private final Runnable toggleMethod;

   public TickBoxControlProcessor(Runnable toggleMethod) {
      this.toggleMethod = toggleMethod;
   }

   public boolean overrideControllerButtons(ScreenProcessor<?> screen, ControllerEntity controller) {
      if (ControlifyBindings.GUI_PRESS.on(controller).guiPressed().get()) {
         this.toggleMethod.run();
         return true;
      } else if (!ControlifyBindings.CYCLE_OPT_FORWARD.on(controller).justPressed() && !ControlifyBindings.CYCLE_OPT_BACKWARD.on(controller).justPressed()) {
         return false;
      } else {
         this.toggleMethod.run();
         return true;
      }
   }
}
