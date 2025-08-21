package dev.isxander.splitscreen.client.features.screenop;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.class_412;
import net.minecraft.class_419;
import net.minecraft.class_424;
import net.minecraft.class_433;
import net.minecraft.class_435;
import net.minecraft.class_437;
import net.minecraft.class_442;
import net.minecraft.class_457;
import org.jetbrains.annotations.Nullable;

public final class ScreenSplitscreenModeRegistry {
   private static final Map<Class<?>, Function<class_437, ScreenSplitscreenMode>> MODES = new HashMap();

   public static void init() {
      if (MODES.isEmpty()) {
         register(class_442.class, ScreenSplitscreenMode.FULLSCREEN);
         register(class_433.class, ScreenSplitscreenMode.SPLITSCREEN);
         register(class_412.class, ScreenSplitscreenMode.SPLITSCREEN);
         register(class_435.class, ScreenSplitscreenMode.SPLITSCREEN);
         register(class_424.class, ScreenSplitscreenMode.SPLITSCREEN);
         register(class_419.class, ScreenSplitscreenMode.FULLSCREEN);
         register(class_457.class, ScreenSplitscreenMode.SPLITSCREEN);
      }
   }

   private static void register(Class<? extends class_437> screenClass, ScreenSplitscreenMode mode) {
      MODES.put(screenClass, (screen) -> {
         return mode;
      });
   }

   private static <T extends class_437> void register(Class<T> screenClass, Function<T, ScreenSplitscreenMode> mode) {
      MODES.put(screenClass, mode);
   }

   public static ScreenSplitscreenMode getMode(@Nullable class_437 screen) {
      if (screen == null) {
         return ScreenSplitscreenMode.SPLITSCREEN;
      } else if (screen instanceof ScreenSplitscreenBehaviour) {
         ScreenSplitscreenBehaviour behaviour = (ScreenSplitscreenBehaviour)screen;
         return behaviour.getSplitscreenMode();
      } else {
         Function<class_437, ScreenSplitscreenMode> modeSupplier = null;

         for(Class clazz = screen.getClass(); modeSupplier == null && clazz != null && class_437.class.isAssignableFrom(clazz); clazz = clazz.getSuperclass()) {
            modeSupplier = (Function)MODES.get(clazz);
         }

         if (modeSupplier == null) {
            return screen.method_25421() ? ScreenSplitscreenMode.FULLSCREEN : ScreenSplitscreenMode.SPLITSCREEN;
         } else {
            return (ScreenSplitscreenMode)modeSupplier.apply(screen);
         }
      }
   }
}
