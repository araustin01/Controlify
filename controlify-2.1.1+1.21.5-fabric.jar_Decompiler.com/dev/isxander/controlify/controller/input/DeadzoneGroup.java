package dev.isxander.controlify.controller.input;

import java.util.List;
import net.minecraft.class_2960;

public record DeadzoneGroup(class_2960 name, List<class_2960> axes) {
   public DeadzoneGroup(class_2960 name, List<class_2960> axes) {
      this.name = name;
      this.axes = axes;
   }

   public class_2960 name() {
      return this.name;
   }

   public List<class_2960> axes() {
      return this.axes;
   }
}
