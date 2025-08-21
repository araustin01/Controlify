package dev.isxander.controlify.screenop.compat.vanilla;

import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.mixins.feature.screenop.vanilla.OptionsSubScreenAccessor;
import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ScreenProcessor;
import net.minecraft.class_1076;
import net.minecraft.class_310;

public class LanguageSelectionListComponentProcessor implements ComponentProcessor {
   private final String code;

   public LanguageSelectionListComponentProcessor(String code) {
      this.code = code;
   }

   public boolean overrideControllerButtons(ScreenProcessor<?> screen, ControllerEntity controller) {
      if (ControlifyBindings.GUI_PRESS.on(controller).justPressed()) {
         class_310 minecraft = class_310.method_1551();
         class_1076 languageManager = minecraft.method_1526();
         if (!this.code.equals(languageManager.method_4669())) {
            languageManager.method_4667(this.code);
            minecraft.field_1690.field_1883 = this.code;
            minecraft.method_1521();
            minecraft.field_1690.method_1640();
         }

         minecraft.method_1507(((OptionsSubScreenAccessor)screen.screen).getLastScreen());
         return true;
      } else {
         return false;
      }
   }
}
