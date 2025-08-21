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
import net.minecraft.class_4267;
import net.minecraft.class_500;
import net.minecraft.class_5244;
import net.minecraft.class_4267.class_504;

public class JoinMultiplayerScreenProcessor extends ScreenProcessor<class_500> {
   private final Supplier<class_4267> listSupplier;

   public JoinMultiplayerScreenProcessor(class_500 screen, Supplier<class_4267> listSupplier) {
      super(screen);
      this.listSupplier = listSupplier;
   }

   protected void handleButtons(ControllerEntity controller) {
      if (ControlifyBindings.GUI_BACK.on(controller).justPressed()) {
         this.getWidget(class_5244.field_24339).ifPresent((back) -> {
            if (!back.method_25370()) {
               class_4267 list = (class_4267)this.listSupplier.get();
               list.method_20122((class_504)null);
               list.method_25395((class_364)null);
               ((class_500)this.screen).method_25395(back);
            } else {
               ((class_4264)back).method_25306();
            }

         });
      }

      super.handleButtons(controller);
   }

   public void onWidgetRebuild() {
      this.getWidget(class_5244.field_24339).ifPresent((button) -> {
         ButtonGuideApi.addGuideToButton((class_4264)button, (InputBindingSupplier)ControlifyBindings.GUI_BACK, ButtonGuidePredicate.always());
      });
      super.onWidgetRebuild();
   }
}
