package dev.isxander.controlify.screenop.compat.vanilla;

import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.mixins.feature.screenop.vanilla.JoinMultiplayerScreenAccessor;
import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ScreenProcessor;

public class ServerSelectionListEntryComponentProcessor implements ComponentProcessor {
   public boolean overrideControllerButtons(ScreenProcessor<?> screen, ControllerEntity controller) {
      if (ControlifyBindings.GUI_PRESS.on(controller).justPressed()) {
         screen.screen.method_25395(((JoinMultiplayerScreenAccessor)screen.screen).getSelectButton());
         return true;
      } else {
         return false;
      }
   }
}
