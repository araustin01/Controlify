package dev.isxander.controlify.controller.input;

import java.util.Set;
import net.minecraft.class_2960;

public interface ControllerStateView {
   boolean isButtonDown(class_2960 var1);

   Set<class_2960> getButtons();

   float getAxisState(class_2960 var1);

   Set<class_2960> getAxes();

   float getAxisResting(class_2960 var1);

   HatState getHatState(class_2960 var1);

   Set<class_2960> getHats();
}
