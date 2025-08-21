package dev.isxander.splitscreen;

import com.mojang.logging.LogUtils;
import dev.isxander.splitscreen.client.SplitscreenBootstrapper;
import dev.isxander.splitscreen.config.SplitscreenServerConfig;
import dev.isxander.splitscreen.server.SplitscreenSSServer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.class_310;
import org.slf4j.Logger;

public class SplitscreenEntrypoint implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static boolean hasRanMain = false;
   private static boolean hasRanClient = false;
   private static boolean hasRanServer = false;

   public void onInitialize() {
      hasRanMain = true;

      for(int i = 0; i < 10; ++i) {
         LOGGER.info("CONTROLIFY SPLITSCREEN CLOSED BETA - DO NOT REDISTRIBUTE!!!");
      }

      SplitscreenSSServer.init();
   }

   public void onInitializeClient() {
      hasRanClient = true;
      SplitscreenBootstrapper.bootstrap(class_310.method_1551());
   }

   public void onInitializeServer() {
      hasRanServer = true;
      SplitscreenServerConfig.INSTANCE.loadFromFile();
   }

   public static boolean hasRanClient() {
      return hasRanClient;
   }
}
