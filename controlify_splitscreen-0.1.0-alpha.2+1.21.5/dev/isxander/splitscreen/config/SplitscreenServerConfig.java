package dev.isxander.splitscreen.config;

import com.mojang.serialization.Codec;
import dev.isxander.yacl3.config.v3.ConfigEntry;
import dev.isxander.yacl3.config.v3.JsonFileCodecConfig;
import net.fabricmc.loader.api.FabricLoader;

public class SplitscreenServerConfig extends JsonFileCodecConfig<SplitscreenServerConfig> {
   public static final SplitscreenServerConfig INSTANCE = new SplitscreenServerConfig();
   public final ConfigEntry<Integer> maxClients;
   public final ConfigEntry<Boolean> allowAnyUsername;
   public final ConfigEntry<Boolean> allowLateLogins;

   public SplitscreenServerConfig() {
      super(FabricLoader.getInstance().getConfigDir().resolve("splitscreen_server.json"));
      this.maxClients = this.register("max_clients", 3, Codec.INT);
      this.allowAnyUsername = this.register("allow_any_username", false, Codec.BOOL);
      this.allowLateLogins = this.register("allow_late_logins", false, Codec.BOOL);
   }
}
