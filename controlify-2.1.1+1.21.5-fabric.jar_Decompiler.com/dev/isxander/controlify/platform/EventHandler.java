package dev.isxander.controlify.platform;

import dev.isxander.controlify.platform.fabric.FabricBackedEventHandler;

public interface EventHandler<T> {
   void register(EventHandler.Callback<T> var1);

   void invoke(T var1);

   static <T> EventHandler<T> createPlatformBackedEvent() {
      return new FabricBackedEventHandler();
   }

   @FunctionalInterface
   public interface Callback<T> {
      void onEvent(T var1);
   }
}
