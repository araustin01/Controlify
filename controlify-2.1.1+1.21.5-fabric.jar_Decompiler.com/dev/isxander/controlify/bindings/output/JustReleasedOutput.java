package dev.isxander.controlify.bindings.output;

import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.bindings.StateAccess;

public class JustReleasedOutput implements DigitalOutput {
   private final StateAccess stateAccess;

   public JustReleasedOutput(InputBinding binding) {
      this.stateAccess = binding.createStateAccess(2);
   }

   public boolean get() {
      if (this.stateAccess.isSuppressed()) {
         return false;
      } else {
         return !this.stateAccess.digital(0) && this.stateAccess.digital(1);
      }
   }
}
