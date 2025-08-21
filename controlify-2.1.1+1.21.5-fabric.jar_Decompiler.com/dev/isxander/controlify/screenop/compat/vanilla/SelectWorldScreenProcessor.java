package dev.isxander.controlify.screenop.compat.vanilla;

import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.mixins.feature.screenop.vanilla.SelectWorldScreenAccessor;
import dev.isxander.controlify.screenop.ScreenProcessor;
import net.minecraft.class_310;
import net.minecraft.class_4185;
import net.minecraft.class_525;
import net.minecraft.class_526;

public class SelectWorldScreenProcessor extends ScreenProcessor<class_526> {
   public SelectWorldScreenProcessor(class_526 screen) {
      super(screen);
   }

   protected void handleButtons(ControllerEntity controller) {
      if (ControlifyBindings.GUI_ABSTRACT_ACTION_1.on(controller).justPressed()) {
         playClackSound();
         class_525.method_31130(class_310.method_1551(), this.screen);
      } else if (((class_526)this.screen).method_25399() != null && ((class_526)this.screen).method_25399() instanceof class_4185 && ControlifyBindings.GUI_BACK.on(controller).justPressed()) {
         ((class_526)this.screen).method_25395(((SelectWorldScreenAccessor)this.screen).getList());
      } else {
         super.handleButtons(controller);
      }
   }
}
