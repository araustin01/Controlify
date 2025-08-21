package dev.isxander.splitscreen.client.engine.impl.reparenting.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_1041;

public interface VanillaWindowFocusEvent {
   Event<VanillaWindowFocusEvent> EVENT = EventFactory.createArrayBacked(VanillaWindowFocusEvent.class, (listeners) -> {
      return (window, focused) -> {
         VanillaWindowFocusEvent[] var3 = listeners;
         int var4 = listeners.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            VanillaWindowFocusEvent listener = var3[var5];
            listener.onFocus(window, focused);
         }

      };
   });

   void onFocus(class_1041 var1, boolean var2);
}
