package dev.isxander.controlify.controller.input;

import java.util.Collection;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_5250;

public final class Inputs {
   private Inputs() {
   }

   public static class_5250 getInputComponent(class_2960 input) {
      String var10000 = input.method_12836();
      return class_2561.method_43471("controlify.input." + var10000 + "." + input.method_12832());
   }

   public static class_5250 getInputComponentAnd(Collection<class_2960> inputs) {
      if (inputs.isEmpty()) {
         return class_2561.method_43473();
      } else {
         class_5250 component = (class_5250)inputs.stream().map(Inputs::getInputComponent).reduce(class_2561.method_43473(), (a, b) -> {
            return a.method_10852(b).method_27693(" + ");
         });
         component.method_10855().remove(component.method_10855().size() - 1);
         return component;
      }
   }
}
