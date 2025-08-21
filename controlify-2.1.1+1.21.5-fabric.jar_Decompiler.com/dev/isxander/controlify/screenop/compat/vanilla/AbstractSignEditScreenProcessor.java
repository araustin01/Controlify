package dev.isxander.controlify.screenop.compat.vanilla;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.InputMode;
import dev.isxander.controlify.api.bind.InputBindingSupplier;
import dev.isxander.controlify.api.buttonguide.ButtonGuideApi;
import dev.isxander.controlify.api.buttonguide.ButtonGuidePredicate;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.screenop.ScreenProcessor;
import net.minecraft.class_4264;
import net.minecraft.class_5244;
import net.minecraft.class_7743;

public class AbstractSignEditScreenProcessor extends ScreenProcessor<class_7743> {
   public AbstractSignEditScreenProcessor(class_7743 screen) {
      super(screen);
   }

   protected void setInitialFocus() {
      if (Controlify.instance().currentInputMode() == InputMode.MIXED) {
         this.holdRepeatHelper.clearDelay();
      } else {
         super.setInitialFocus();
      }

   }

   public void onWidgetRebuild() {
      super.onWidgetRebuild();
      this.getWidget(class_5244.field_24334).ifPresent((doneButton) -> {
         ButtonGuideApi.addGuideToButton((class_4264)doneButton, (InputBindingSupplier)ControlifyBindings.GUI_BACK, ButtonGuidePredicate.always());
      });
   }
}
