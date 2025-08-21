package dev.isxander.controlify.screenkeyboard;

import net.minecraft.class_408;

public interface ChatKeyboardDucky {
   float controlify$keyboardShiftAmount();

   static float getKeyboardShiftAmount(class_408 screen) {
      return ((ChatKeyboardDucky)screen).controlify$keyboardShiftAmount();
   }
}
