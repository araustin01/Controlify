package dev.isxander.controlify.platform.fabric;

import dev.isxander.controlify.platform.EventHandler;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class FabricBackedEventHandler<T> implements EventHandler<T> {
   private final Event<EventHandler.Callback<T>> backedEvent = EventFactory.createArrayBacked(EventHandler.Callback.class, (callbacks) -> {
      return (event) -> {
         EventHandler.Callback[] var2 = callbacks;
         int var3 = callbacks.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            EventHandler.Callback<T> callback = var2[var4];
            callback.onEvent(event);
         }

      };
   });

   public void register(EventHandler.Callback<T> event) {
      this.backedEvent.register(event);
   }

   public void invoke(T event) {
      ((EventHandler.Callback)this.backedEvent.invoker()).onEvent(event);
   }
}
