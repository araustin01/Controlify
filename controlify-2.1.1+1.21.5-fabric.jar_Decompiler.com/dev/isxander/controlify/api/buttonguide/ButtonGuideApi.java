package dev.isxander.controlify.api.buttonguide;

import dev.isxander.controlify.api.bind.InputBindingSupplier;
import dev.isxander.controlify.gui.ButtonGuideRenderer;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

public final class ButtonGuideApi {
   public static <T> void addGuideToButton(@NotNull T button, @NotNull InputBindingSupplier binding, @NotNull ButtonGuidePredicate<T> renderPredicate) {
      ButtonGuideRenderer.registerBindingForButton(button, () -> {
         return binding;
      }, renderPredicate);
   }

   public static <T> void addGuideToButton(@NotNull T button, @NotNull Supplier<InputBindingSupplier> binding, @NotNull ButtonGuidePredicate<T> renderPredicate) {
      ButtonGuideRenderer.registerBindingForButton(button, binding, renderPredicate);
   }
}
