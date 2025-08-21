package dev.isxander.controlify.screenop;

import net.minecraft.class_437;
import org.jetbrains.annotations.NotNull;

public interface ScreenProcessorProvider {
   ScreenProcessor<?> screenProcessor();

   static ScreenProcessor<?> provide(@NotNull class_437 screen) {
      return ((ScreenProcessorProvider)screen).screenProcessor();
   }

   static <T extends class_437> void registerProvider(@NotNull Class<T> screenClass, @NotNull ScreenProcessorFactory.Factory<T> factory) {
      ScreenProcessorFactory.registerProvider(screenClass, factory);
   }
}
