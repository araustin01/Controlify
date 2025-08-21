package dev.isxander.controlify.api.bind;

import net.minecraft.class_332;

@FunctionalInterface
public interface RadialIcon {
   RadialIcon EMPTY = (graphics, x, y, tickDelta) -> {
   };

   void draw(class_332 var1, int var2, int var3, float var4);
}
