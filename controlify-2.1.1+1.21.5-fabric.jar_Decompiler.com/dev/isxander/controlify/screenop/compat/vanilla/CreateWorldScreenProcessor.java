package dev.isxander.controlify.screenop.compat.vanilla;

import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.screenop.ScreenProcessor;
import net.minecraft.class_525;

public class CreateWorldScreenProcessor extends ScreenProcessor<class_525> {
   private final Runnable onCreateButton;

   public CreateWorldScreenProcessor(class_525 screen, Runnable onCreateButton) {
      super(screen);
      this.onCreateButton = onCreateButton;
   }

   protected void handleButtons(ControllerEntity controller) {
      if (ControlifyBindings.GUI_ABSTRACT_ACTION_1.on(controller).justPressed()) {
         this.onCreateButton.run();
         playClackSound();
      }

      super.handleButtons(controller);
   }
}
