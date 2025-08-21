package dev.isxander.controlify.gui;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.api.bind.InputBindingSupplier;
import dev.isxander.controlify.api.buttonguide.ButtonGuidePredicate;
import dev.isxander.controlify.controller.GenericControllerConfig;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_5244;
import net.minecraft.class_5250;

public interface ButtonGuideRenderer<T> {
   void controlify$setButtonGuide(ButtonGuideRenderer.RenderData<T> var1);

   static <T> void registerBindingForButton(T button, Supplier<InputBindingSupplier> binding, ButtonGuidePredicate<T> renderPredicate) {
      ((ButtonGuideRenderer)button).controlify$setButtonGuide(new ButtonGuideRenderer.RenderData(binding, renderPredicate));
   }

   public static record RenderData<T>(Supplier<InputBindingSupplier> binding, ButtonGuidePredicate<T> renderPredicate) {
      public RenderData(Supplier<InputBindingSupplier> binding, ButtonGuidePredicate<T> renderPredicate) {
         this.binding = binding;
         this.renderPredicate = renderPredicate;
      }

      public class_2561 getControllerMessage(InputBinding bind, class_2561 actualLabel) {
         class_5250 component = class_2561.method_43473();
         if (!class_310.method_1551().field_1772.method_1726()) {
            component.method_10852(bind.inputIcon());
            component.method_10852(class_5244.field_41874);
         }

         component.method_10852(actualLabel);
         if (class_310.method_1551().field_1772.method_1726()) {
            component.method_10852(class_5244.field_41874);
            component.method_10852(bind.inputIcon());
         }

         return component;
      }

      public boolean shouldRender(T renderable) {
         Optional<InputBinding> binding = this.getBind();
         return binding.isPresent() && Controlify.instance().currentInputMode().isController() && (Boolean)Controlify.instance().getCurrentController().map((c) -> {
            return ((GenericControllerConfig)c.genericConfig().config()).showScreenGuides;
         }).orElse(false) && !((InputBinding)binding.get()).isUnbound() && this.renderPredicate().shouldDisplay(renderable);
      }

      public Optional<InputBinding> getBind() {
         return Controlify.instance().getCurrentController().map((c) -> {
            return ((InputBindingSupplier)this.binding().get()).on(c);
         });
      }

      public Supplier<InputBindingSupplier> binding() {
         return this.binding;
      }

      public ButtonGuidePredicate<T> renderPredicate() {
         return this.renderPredicate;
      }
   }
}
