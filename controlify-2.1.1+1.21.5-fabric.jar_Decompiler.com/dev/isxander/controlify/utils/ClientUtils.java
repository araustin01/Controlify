package dev.isxander.controlify.utils;

import dev.isxander.controlify.utils.render.Blit;
import java.util.Objects;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_7077;
import net.minecraft.class_7842;
import net.minecraft.class_4185.class_4241;

public final class ClientUtils {
   private static final class_2960 GREEN_BACK_BAR = class_2960.method_60656("boss_bar/green_background");
   private static final class_2960 GREEN_FRONT_BAR = class_2960.method_60656("boss_bar/green_progress");

   private ClientUtils() {
   }

   public static class_7842 createStringWidget(class_2561 text, class_327 font, int x, int y) {
      int var10004 = font.method_30880(text.method_30937());
      Objects.requireNonNull(font);
      return new class_7842(x, y, var10004, 9, text, font);
   }

   public static class_7077 createPlainTextButton(class_2561 text, class_327 font, int x, int y, class_4241 onPress) {
      int var10004 = font.method_30880(text.method_30937());
      Objects.requireNonNull(font);
      return new class_7077(x, y, var10004, 9, text, onPress, font);
   }

   public static void drawSprite(class_332 graphics, class_2960 location, int x, int y, int width, int height) {
      Blit.sprite(graphics, location, x, y, width, height);
   }

   public static void drawBar(class_332 graphics, int centerX, int y, float progress) {
      int width = (int)class_3532.method_37166(0.0F, 182.0F, progress);
      int x = centerX - 91;
      Blit.sprite(graphics, GREEN_BACK_BAR, 182, 5, 0, 0, x, y, 182, 5);
      if (width > 0) {
         Blit.sprite(graphics, GREEN_FRONT_BAR, 182, 5, 0, 0, x, y, width, 5);
      }

   }
}
