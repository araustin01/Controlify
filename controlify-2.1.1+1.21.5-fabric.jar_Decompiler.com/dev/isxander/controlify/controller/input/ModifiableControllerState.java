package dev.isxander.controlify.controller.input;

import net.minecraft.class_2960;

public interface ModifiableControllerState extends ControllerState {
   void setButton(class_2960 var1, boolean var2);

   void setAxis(class_2960 var1, float var2);

   void setHat(class_2960 var1, HatState var2);
}
