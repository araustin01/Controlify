package dev.isxander.controlify.utils;

import java.util.function.Function;
import net.minecraft.class_310;
import net.minecraft.class_437;

public interface InitialScreenRegistryDuck {
   void controlify$registerInitialScreen(Function<Runnable, class_437> var1);

   static void registerInitialScreen(Function<Runnable, class_437> screenFactory) {
      ((InitialScreenRegistryDuck)class_310.method_1551()).controlify$registerInitialScreen(screenFactory);
   }
}
