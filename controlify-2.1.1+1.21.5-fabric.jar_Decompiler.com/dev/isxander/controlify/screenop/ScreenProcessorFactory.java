package dev.isxander.controlify.screenop;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.class_437;

public final class ScreenProcessorFactory {
   private static final Map<Class<? extends class_437>, ScreenProcessorFactory.Factory<?>> factories = new Object2ObjectOpenHashMap();

   private ScreenProcessorFactory() {
   }

   public static <T extends class_437> ScreenProcessor<? super T> createForScreen(T screen) {
      Class screenClass;
      for(screenClass = screen.getClass(); !factories.containsKey(screenClass) && screenClass != class_437.class; screenClass = screenClass.getSuperclass()) {
      }

      return (ScreenProcessor)((ScreenProcessorFactory.Factory)factories.getOrDefault(screenClass, ScreenProcessorFactory::createDefault)).apply(screen);
   }

   public static <T extends class_437> void registerProvider(Class<T> screenClass, ScreenProcessorFactory.Factory<T> factory) {
      factories.put(screenClass, factory);
   }

   private static <T extends class_437> ScreenProcessor<T> createDefault(T screen) {
      return new ScreenProcessor(screen);
   }

   public interface Factory<T extends class_437> extends Function<T, ScreenProcessor<? super T>> {
   }
}
