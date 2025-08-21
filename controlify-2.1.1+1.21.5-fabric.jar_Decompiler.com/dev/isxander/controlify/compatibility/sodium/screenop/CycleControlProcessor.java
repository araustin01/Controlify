package dev.isxander.controlify.compatibility.sodium.screenop;

import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ScreenProcessor;
import java.util.function.Consumer;

public class CycleControlProcessor implements ComponentProcessor {
   private final Consumer<Boolean> cycleMethod;

   public CycleControlProcessor(Consumer<Boolean> cycleMethod) {
      this.cycleMethod = cycleMethod;
   }

   public boolean overrideControllerButtons(ScreenProcessor<?> screen, ControllerEntity controller) {
      if (!ControlifyBindings.CYCLE_OPT_FORWARD.on(controller).justPressed() && !ControlifyBindings.GUI_PRESS.on(controller).justPressed()) {
         if (ControlifyBindings.CYCLE_OPT_BACKWARD.on(controller).justPressed()) {
            this.cycleMethod.accept(true);
            return true;
         } else {
            return false;
         }
      } else {
         this.cycleMethod.accept(false);
         return true;
      }
   }
}
