package dev.isxander.controlify.bindings.output;

import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.bindings.KeyMappingHandle;
import dev.isxander.controlify.bindings.StateAccess;
import dev.isxander.controlify.controller.ControllerEntity;
import java.util.function.BooleanSupplier;
import net.minecraft.class_304;
import net.minecraft.class_310;

public class KeyMappingEmulationOutput implements DigitalOutput {
   private final ControllerEntity controller;
   private final StateAccess stateAccess;
   private final class_304 keyMapping;

   public KeyMappingEmulationOutput(ControllerEntity controller, InputBinding binding, class_304 keyMapping, BooleanSupplier toggleCondition) {
      this.controller = controller;
      this.stateAccess = binding.createStateAccess(2, (state) -> {
         this.push();
      });
      this.keyMapping = keyMapping;
      if (toggleCondition != null) {
         ((KeyMappingHandle)keyMapping).controlify$addToggleCondition(controller, toggleCondition);
      }

   }

   public boolean get() {
      throw new IllegalStateException("Should never retrieve output of key mapping emulation!");
   }

   private void push() {
      boolean now = this.stateAccess.digital(0);
      boolean prev = this.stateAccess.digital(1);
      if (ControlifyApi.get().getCurrentController().orElse((Object)null) == this.controller) {
         if (class_310.method_1551().field_1755 == null) {
            KeyMappingHandle handle = (KeyMappingHandle)this.keyMapping;
            if (now && !prev) {
               handle.controlify$setPressed(true);
            } else if (prev && !now) {
               handle.controlify$setPressed(false);
            }

         }
      }
   }
}
