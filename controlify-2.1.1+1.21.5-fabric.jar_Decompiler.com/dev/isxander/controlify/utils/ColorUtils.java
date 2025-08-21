package dev.isxander.controlify.utils;

import net.minecraft.class_3532;
import net.minecraft.class_9848;

public final class ColorUtils {
   public static int lerpARGB(float delta, int start, int end) {
      return class_9848.method_61319(delta, start, end);
   }

   public static int argbRed(int argb) {
      return class_9848.method_61327(argb);
   }

   public static int argbGreen(int argb) {
      return class_9848.method_61329(argb);
   }

   public static int argbBlue(int argb) {
      return class_9848.method_61331(argb);
   }

   public static int argbAlpha(int argb) {
      return class_9848.method_61320(argb);
   }

   public static float[] decomposeARGBFloat(int argb) {
      return new float[]{(float)class_9848.method_61320(argb) / 255.0F, (float)class_9848.method_61327(argb) / 255.0F, (float)class_9848.method_61329(argb) / 255.0F, (float)class_9848.method_61331(argb) / 255.0F};
   }

   public static int grey(float brightness, float alpha) {
      int component = class_3532.method_15375(brightness * 255.0F);
      int color = class_3532.method_15375(alpha * 255.0F);
      color = color << 8 | component;
      color = color << 8 | component;
      color = color << 8 | component;
      return color;
   }
}
