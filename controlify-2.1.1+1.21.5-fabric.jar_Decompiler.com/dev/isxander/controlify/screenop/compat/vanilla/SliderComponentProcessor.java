package dev.isxander.controlify.screenop.compat.vanilla;

import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ScreenProcessor;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.class_357;

public class SliderComponentProcessor implements ComponentProcessor {
   private final Supplier<Boolean> canChangeValueGetter;
   private final Consumer<Boolean> canChangeValueSetter;
   private final class_357 component;
   private static final int SLIDER_CHANGE_DELAY = 1;
   private int lastSliderChange = 1;

   public SliderComponentProcessor(class_357 component, Supplier<Boolean> canChangeValueGetter, Consumer<Boolean> canChangeValueSetter) {
      this.component = component;
      this.canChangeValueGetter = canChangeValueGetter;
      this.canChangeValueSetter = canChangeValueSetter;
   }

   public boolean overrideControllerNavigation(ScreenProcessor<?> screen, ControllerEntity controller) {
      if (!(Boolean)this.canChangeValueGetter.get()) {
         return false;
      } else {
         boolean canSliderChange = ++this.lastSliderChange > 1;
         if (ControlifyBindings.GUI_NAVI_RIGHT.on(controller).digitalNow()) {
            if (canSliderChange) {
               this.component.method_25404(262, 0, 0);
               this.lastSliderChange = 0;
            }

            return true;
         } else if (ControlifyBindings.GUI_NAVI_LEFT.on(controller).digitalNow()) {
            if (canSliderChange) {
               this.component.method_25404(263, 0, 0);
               this.lastSliderChange = 0;
            }

            return true;
         } else {
            return false;
         }
      }
   }

   public boolean overrideControllerButtons(ScreenProcessor<?> screen, ControllerEntity controller) {
      if (!(Boolean)this.canChangeValueGetter.get()) {
         return false;
      } else if (ControlifyBindings.GUI_BACK.on(controller).justPressed()) {
         this.canChangeValueSetter.accept(false);
         return true;
      } else {
         return false;
      }
   }

   public void onFocusGained(ScreenProcessor<?> screen, ControllerEntity controller) {
      this.canChangeValueSetter.accept(false);
   }
}
