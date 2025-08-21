package dev.isxander.controlify.gui.screen;

import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_403;
import net.minecraft.class_437;
import net.minecraft.class_5244;

public class NoSDLScreen extends class_403 {
   public NoSDLScreen(Runnable actionHandler, class_437 parent) {
      super(() -> {
         actionHandler.run();
         class_310.method_1551().method_1507(parent);
      }, class_2561.method_43471("controlify.gui.no_sdl.title"), class_2561.method_43471("controlify.gui.no_sdl.message"), class_5244.field_44914, false);
   }
}
