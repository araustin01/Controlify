package dev.isxander.controlify.utils;

import net.minecraft.class_3532;

public final class MthExt {
   private MthExt() {
   }

   public static float remap(float value, float minIn, float maxIn, float minOut, float maxOut) {
      return class_3532.method_16439(class_3532.method_37960(value, minIn, maxIn), minOut, maxOut);
   }
}
