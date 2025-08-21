package dev.isxander.controlify.controller.input;

import dev.isxander.controlify.utils.ControllerUtils;
import java.util.Optional;
import java.util.Set;
import net.minecraft.class_2960;

public class DeadzoneControllerStateView implements ControllerStateView {
   private final ControllerStateView view;
   private final InputComponent input;

   public DeadzoneControllerStateView(ControllerStateView view, InputComponent input) {
      this.view = view;
      this.input = input;
   }

   public boolean isButtonDown(class_2960 button) {
      return this.view.isButtonDown(button);
   }

   public Set<class_2960> getButtons() {
      return this.view.getButtons();
   }

   public float getAxisState(class_2960 axis) {
      float rawAxis = this.view.getAxisState(axis);
      Optional<class_2960> deadzoneId = this.input.getDeadzoneForAxis(axis);
      float deadzone = (Float)deadzoneId.map((id) -> {
         return (Float)((InputComponent.Config)this.input.confObj()).deadzones.get(id);
      }).orElse(0.0F);
      return ControllerUtils.deadzone(rawAxis, deadzone);
   }

   public Set<class_2960> getAxes() {
      return this.view.getAxes();
   }

   public float getAxisResting(class_2960 axis) {
      return this.view.getAxisResting(axis);
   }

   public HatState getHatState(class_2960 hat) {
      return this.view.getHatState(hat);
   }

   public Set<class_2960> getHats() {
      return this.view.getHats();
   }
}
