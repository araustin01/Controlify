package dev.isxander.controlify.bindings.output;

import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.bindings.StateAccess;

public class GuiPressOutput implements DigitalOutput {
   private final StateAccess stateAccess;
   private GuiPressOutput.PressState pressState;

   public GuiPressOutput(InputBinding binding) {
      this.pressState = GuiPressOutput.PressState.OFF;
      this.stateAccess = binding.createStateAccess(2, (state) -> {
         this.push();
      });
   }

   public boolean get() {
      return this.pressState == GuiPressOutput.PressState.JUST_RELEASED;
   }

   public GuiPressOutput.PressState getPressState() {
      return this.pressState;
   }

   private void push() {
      boolean held = this.stateAccess.digital(0);
      boolean prevHeld = this.stateAccess.digital(1);
      if (held) {
         if (!prevHeld) {
            this.pressState = GuiPressOutput.PressState.COULD_PRESS_IN_FUTURE;
         }
      } else if (prevHeld && this.pressState == GuiPressOutput.PressState.COULD_PRESS_IN_FUTURE) {
         this.pressState = GuiPressOutput.PressState.JUST_RELEASED;
      } else {
         this.pressState = GuiPressOutput.PressState.OFF;
      }

      if (this.stateAccess.isSuppressed()) {
         this.pressState = GuiPressOutput.PressState.OFF;
      }

   }

   public void onNavigate() {
      this.pressState = GuiPressOutput.PressState.OFF;
   }

   public static enum PressState {
      OFF,
      JUST_RELEASED,
      COULD_PRESS_IN_FUTURE;

      // $FF: synthetic method
      private static GuiPressOutput.PressState[] $values() {
         return new GuiPressOutput.PressState[]{OFF, JUST_RELEASED, COULD_PRESS_IN_FUTURE};
      }
   }
}
