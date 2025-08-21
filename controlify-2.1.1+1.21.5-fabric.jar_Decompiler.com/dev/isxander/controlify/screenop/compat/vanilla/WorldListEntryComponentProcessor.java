package dev.isxander.controlify.screenop.compat.vanilla;

import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.mixins.feature.screenop.vanilla.SelectWorldScreenAccessor;
import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ScreenProcessor;
import net.minecraft.class_526;

public class WorldListEntryComponentProcessor implements ComponentProcessor {
   public boolean overrideControllerButtons(ScreenProcessor<?> screen, ControllerEntity controller) {
      if (ControlifyBindings.GUI_PRESS.on(controller).justPressed()) {
         class_526 selectWorldScreen = (class_526)screen.screen;
         selectWorldScreen.method_25395(((SelectWorldScreenAccessor)selectWorldScreen).getSelectButton());
         return true;
      } else {
         return false;
      }
   }
}
