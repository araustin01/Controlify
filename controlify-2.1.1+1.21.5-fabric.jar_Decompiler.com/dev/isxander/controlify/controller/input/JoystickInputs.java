package dev.isxander.controlify.controller.input;

import dev.isxander.controlify.utils.CUtil;
import net.minecraft.class_2960;

public final class JoystickInputs {
   private static final class_2960[] BUTTONS = new class_2960[256];
   private static final class_2960[] AXES = new class_2960[512];
   private static final class_2960[] HATS = new class_2960[256];

   private JoystickInputs() {
   }

   public static class_2960 button(int index) {
      class_2960 cache = BUTTONS[index];
      if (cache == null) {
         BUTTONS[index] = cache = CUtil.rl("button/" + index);
      }

      return cache;
   }

   public static class_2960 axis(int index, boolean positive) {
      int cacheIndex = index;
      if (!positive) {
         cacheIndex = index + 256;
      }

      class_2960 cache = AXES[cacheIndex];
      if (cache == null) {
         AXES[cacheIndex] = cache = CUtil.rl("axis/" + index + "/" + (positive ? "positive" : "negative"));
      }

      return cache;
   }

   public static class_2960 hat(int index) {
      class_2960 cache = HATS[index];
      if (cache == null) {
         HATS[index] = cache = CUtil.rl("hat/" + index);
      }

      return cache;
   }
}
