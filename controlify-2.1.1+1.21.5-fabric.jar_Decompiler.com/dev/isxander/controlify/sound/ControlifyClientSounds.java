package dev.isxander.controlify.sound;

import dev.isxander.controlify.utils.CUtil;
import java.util.function.Supplier;
import net.minecraft.class_2960;
import net.minecraft.class_3414;

public final class ControlifyClientSounds {
   public static final Supplier<class_3414> SCREEN_FOCUS_CHANGE = register("controlify.ui.focus");

   private static Supplier<class_3414> register(String id) {
      class_2960 location = CUtil.rl(id);
      class_3414 sound = class_3414.method_47908(location);
      return () -> {
         return sound;
      };
   }

   public static void init() {
   }
}
