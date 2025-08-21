package dev.isxander.controlify.platform;

import java.util.ArrayList;
import java.util.List;

public class ArrayBackedEventHandler<T> implements EventHandler<T> {
   private final List<EventHandler.Callback<T>> callbacks = new ArrayList();

   public void register(EventHandler.Callback<T> event) {
      this.callbacks.add(event);
   }

   public void invoke(T event) {
      this.callbacks.forEach((c) -> {
         c.onEvent(event);
      });
   }
}
