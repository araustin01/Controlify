package dev.isxander.controlify.gui.layout;

import net.minecraft.class_332;
import org.joml.Vector2ic;

public interface RenderComponent {
   void render(class_332 var1, int var2, int var3, float var4);

   Vector2ic size();

   default boolean isVisible() {
      return true;
   }
}
