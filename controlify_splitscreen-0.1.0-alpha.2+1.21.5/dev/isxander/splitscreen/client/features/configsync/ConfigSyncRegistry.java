package dev.isxander.splitscreen.client.features.configsync;

import dev.isxander.splitscreen.client.SplitscreenBootstrapper;
import dev.isxander.splitscreen.client.config.SplitscreenConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.class_2960;

public final class ConfigSyncRegistry {
   private static final Map<class_2960, Runnable> savers = new HashMap();

   public static void registerSaver(class_2960 location, Runnable saver) {
      if (savers.containsKey(location)) {
         throw new IllegalArgumentException("Duplicate saver location: " + String.valueOf(location));
      } else {
         savers.put(location, saver);
      }
   }

   public static void onSave(class_2960 location) {
      SplitscreenBootstrapper.getController().ifPresent((controller) -> {
         controller.forEachPawn((pawn) -> {
            pawn.onConfigSave(location);
         });
      });
   }

   private ConfigSyncRegistry() {
   }

   static {
      class_2960 var10000 = SplitscreenConfig.CONFIG_ID;
      SplitscreenConfig var10001 = SplitscreenConfig.INSTANCE;
      Objects.requireNonNull(var10001);
      registerSaver(var10000, var10001::loadFromFile);
   }
}
