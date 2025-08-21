package dev.isxander.controlify.utils.render;

import java.util.function.Function;
import net.minecraft.class_1058;
import net.minecraft.class_1921;
import net.minecraft.class_2960;
import net.minecraft.class_332;

public final class Blit {
   public static void batchDraw(class_332 graphics, Runnable renderer) {
      graphics.method_64039((bufferSource) -> {
         renderer.run();
      });
   }

   public static void tex(class_332 graphics, class_2960 texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
      graphics.method_25290(guiTextured(), texture, x, y, u, v, width, height, textureWidth, textureHeight);
   }

   public static void tex(class_332 graphics, class_2960 atlasLocation, int x, int y, int textureX, int textureY, int width, int height, int atlasWidth, int atlasHeight) {
      graphics.method_25290(guiTextured(), atlasLocation, x, y, (float)textureX, (float)textureY, width, height, atlasWidth, atlasHeight);
   }

   public static void tex(class_332 graphics, class_2960 texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, int color) {
      graphics.method_25291(guiTextured(), texture, x, y, u, v, width, height, textureWidth, textureHeight, color);
   }

   public static void sprite(class_332 graphics, class_2960 sprite, int x, int y, int width, int height) {
      graphics.method_52706(guiTextured(), sprite, x, y, width, height);
   }

   public static void sprite(class_332 graphics, class_2960 sprite, int textureWidth, int textureHeight, int u, int v, int x, int y, int width, int height) {
      graphics.method_52708(guiTextured(), sprite, textureWidth, textureHeight, u, v, x, y, width, height);
   }

   public static void sprite(class_332 graphics, class_1058 sprite, int x, int y, int width, int height, int color) {
      graphics.method_52710(guiTextured(), sprite, x, y, width, height, color);
   }

   public static Function<class_2960, class_1921> guiTextured() {
      return class_1921::method_62277;
   }
}
