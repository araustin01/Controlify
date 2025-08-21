package dev.isxander.controlify.driver;

import dev.isxander.controlify.controller.ControllerEntity;
import java.util.function.Consumer;

public class ComponentAdderDriver implements Driver {
   private final Consumer<ControllerEntity> componentAdder;

   public ComponentAdderDriver(Consumer<ControllerEntity> componentAdder) {
      this.componentAdder = componentAdder;
   }

   public void addComponents(ControllerEntity controller) {
      this.componentAdder.accept(controller);
   }
}
