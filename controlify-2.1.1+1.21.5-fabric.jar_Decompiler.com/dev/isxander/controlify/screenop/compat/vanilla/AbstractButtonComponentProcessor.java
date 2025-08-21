package dev.isxander.controlify.screenop.compat.vanilla;

import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.haptic.HapticEffects;
import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ScreenProcessor;
import net.minecraft.class_310;
import net.minecraft.class_4264;

public class AbstractButtonComponentProcessor implements ComponentProcessor {
   private final class_4264 button;

   public AbstractButtonComponentProcessor(class_4264 button) {
      this.button = button;
   }

   public boolean overrideControllerButtons(ScreenProcessor<?> screen, ControllerEntity controller) {
      if (ControlifyBindings.GUI_PRESS.on(controller).guiPressed().get()) {
         controller.hdHaptics().ifPresent((hh) -> {
            hh.playHaptic(HapticEffects.NAVIGATE);
         });
         this.button.method_25354(class_310.method_1551().method_1483());
         this.button.method_25306();
         return true;
      } else {
         return false;
      }
   }
}
