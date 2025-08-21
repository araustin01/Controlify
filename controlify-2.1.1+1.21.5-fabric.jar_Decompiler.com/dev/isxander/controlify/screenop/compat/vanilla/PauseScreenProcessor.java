package dev.isxander.controlify.screenop.compat.vanilla;

import dev.isxander.controlify.api.bind.InputBindingSupplier;
import dev.isxander.controlify.api.buttonguide.ButtonGuideApi;
import dev.isxander.controlify.api.buttonguide.ButtonGuidePredicate;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.mixins.feature.screenop.vanilla.PauseScreenAccessor;
import dev.isxander.controlify.screenop.ScreenProcessor;
import java.util.function.Supplier;
import net.minecraft.class_364;
import net.minecraft.class_4185;
import net.minecraft.class_4264;
import net.minecraft.class_429;
import net.minecraft.class_433;

public class PauseScreenProcessor extends ScreenProcessor<class_433> {
   private final Supplier<class_4185> disconnectButtonSupplier;

   public PauseScreenProcessor(class_433 screen, Supplier<class_4185> disconnectButtonSupplier) {
      super(screen);
      this.disconnectButtonSupplier = disconnectButtonSupplier;
   }

   protected void handleButtons(ControllerEntity controller) {
      super.handleButtons(controller);
      if (ControlifyBindings.GUI_ABSTRACT_ACTION_1.on(controller).justPressed()) {
         minecraft.method_1507(new class_429(this.screen, minecraft.field_1690));
      }

      if (ControlifyBindings.GUI_ABSTRACT_ACTION_2.on(controller).justPressed()) {
         ((class_433)this.screen).method_25395((class_364)this.disconnectButtonSupplier.get());
      }

   }

   public void onWidgetRebuild() {
      super.onWidgetRebuild();
      if (((PauseScreenAccessor)this.screen).getShowPauseMenu()) {
         this.getWidget("menu.returnToGame").ifPresent((widget) -> {
            ButtonGuideApi.addGuideToButton((class_4264)widget, (InputBindingSupplier)ControlifyBindings.GUI_BACK, ButtonGuidePredicate.always());
         });
         this.getWidget("menu.options").ifPresent((widget) -> {
            ButtonGuideApi.addGuideToButton((class_4264)widget, (InputBindingSupplier)ControlifyBindings.GUI_ABSTRACT_ACTION_1, ButtonGuidePredicate.always());
         });
         class_4185 disconnectButton = (class_4185)this.disconnectButtonSupplier.get();
         if (disconnectButton != null) {
            ButtonGuideApi.addGuideToButton(disconnectButton, (Supplier)(() -> {
               return disconnectButton.method_25370() ? ControlifyBindings.GUI_PRESS : ControlifyBindings.GUI_ABSTRACT_ACTION_2;
            }), ButtonGuidePredicate.always());
         }
      }

   }
}
