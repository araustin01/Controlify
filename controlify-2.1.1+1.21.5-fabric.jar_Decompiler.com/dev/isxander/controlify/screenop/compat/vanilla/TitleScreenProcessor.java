package dev.isxander.controlify.screenop.compat.vanilla;

import dev.isxander.controlify.api.bind.InputBindingSupplier;
import dev.isxander.controlify.api.buttonguide.ButtonGuideApi;
import dev.isxander.controlify.api.buttonguide.ButtonGuidePredicate;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.screenop.ScreenProcessor;
import java.util.function.Supplier;
import net.minecraft.class_364;
import net.minecraft.class_4264;
import net.minecraft.class_429;
import net.minecraft.class_442;

public class TitleScreenProcessor extends ScreenProcessor<class_442> {
   public TitleScreenProcessor(class_442 screen) {
      super(screen);
   }

   protected void handleButtons(ControllerEntity controller) {
      if (ControlifyBindings.GUI_BACK.on(controller).justPressed()) {
         ((class_442)this.screen).method_25395((class_364)this.getWidget("menu.quit").orElseThrow());
         playClackSound();
      }

      super.handleButtons(controller);
      if (ControlifyBindings.GUI_ABSTRACT_ACTION_1.on(controller).justPressed()) {
         minecraft.method_1507(new class_429(this.screen, minecraft.field_1690));
         playClackSound();
      }

   }

   public void onWidgetRebuild() {
      super.onWidgetRebuild();
      class_4264 quitButton = (class_4264)this.getWidget("menu.quit").orElseThrow();
      ButtonGuideApi.addGuideToButton(quitButton, (Supplier)(() -> {
         return quitButton.method_25370() ? ControlifyBindings.GUI_PRESS : ControlifyBindings.GUI_BACK;
      }), ButtonGuidePredicate.always());
      ButtonGuideApi.addGuideToButton((class_4264)this.getWidget("menu.options").orElseThrow(), (InputBindingSupplier)ControlifyBindings.GUI_ABSTRACT_ACTION_1, ButtonGuidePredicate.always());
   }
}
