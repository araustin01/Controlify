package dev.isxander.controlify.gui.screen;

import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.input.InputComponent;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_410;
import net.minecraft.class_437;

public class AskToMapControllerScreen extends class_410 {
   public AskToMapControllerScreen(ControllerEntity controller, class_437 lastScreen) {
      super((confirmed) -> {
         if (confirmed) {
            class_310.method_1551().method_1507(ControllerMappingMakerScreen.createGamepadMapping((InputComponent)controller.input().orElseThrow(), lastScreen));
         } else {
            class_310.method_1551().method_1507(lastScreen);
         }

      }, class_2561.method_43471("controlify.ask_to_map_controller.title"), class_2561.method_43471("controlify.ask_to_map_controller.message"), class_2561.method_43471("controlify.ask_to_map_controller.yes"), class_2561.method_43471("controlify.ask_to_map_controller.no"));
   }
}
