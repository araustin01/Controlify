package dev.isxander.controlify.screenop;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.jetbrains.annotations.ApiStatus.Internal;

public class Registry<T, U> {
   private final Map<Class<? extends T>, Function<T, U>> registry = new Object2ObjectOpenHashMap();
   private final Map<T, U> cache = new Object2ObjectOpenHashMap();

   public <V extends T> void register(Class<V> clazz, Function<V, U> constructor) {
      this.registry.put(clazz, constructor);
   }

   Optional<U> get(T object) {
      U cached = this.cache.get(object);
      if (cached != null) {
         return Optional.of(cached);
      } else {
         Class<? extends T> clazz = object.getClass();
         Function<T, U> constructor = (Function)this.registry.get(clazz);
         if (constructor == null) {
            return Optional.empty();
         } else {
            U constructed = constructor.apply(object);
            this.cache.put(object, constructed);
            return Optional.of(constructed);
         }
      }
   }

   @Internal
   public void clearCache() {
      this.cache.clear();
   }
}
