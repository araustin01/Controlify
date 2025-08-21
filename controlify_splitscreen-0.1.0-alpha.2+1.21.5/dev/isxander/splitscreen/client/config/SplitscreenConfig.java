package dev.isxander.splitscreen.client.config;

import com.mojang.serialization.Codec;
import dev.isxander.splitscreen.client.features.configsync.ConfigSyncRegistry;
import dev.isxander.splitscreen.config.AudioMethod;
import dev.isxander.splitscreen.config.MusicMethod;
import dev.isxander.splitscreen.config.SplitscreenServerConfig;
import dev.isxander.splitscreen.config.SplitscreenServerSharedConfig;
import dev.isxander.splitscreen.util.CSUtil;
import dev.isxander.yacl3.config.v3.ConfigEntry;
import dev.isxander.yacl3.config.v3.JsonFileCodecConfig;
import dev.isxander.yacl3.config.v3.ReadonlyConfigEntry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_2960;

public class SplitscreenConfig extends JsonFileCodecConfig<SplitscreenConfig> {
   public static final SplitscreenConfig INSTANCE = new SplitscreenConfig();
   public static final class_2960 CONFIG_ID = CSUtil.rl("config");
   public final ConfigEntry<Boolean> preferVerticalSplitscreen;
   public final ConfigEntry<AudioMethod> audioMethod;
   public final ConfigEntry<MusicMethod> musicMethod;
   public final ReadonlyConfigEntry<SplitscreenServerConfig> serverConfig;

   public SplitscreenConfig() {
      super(FabricLoader.getInstance().getConfigDir().resolve("splitscreen.json"));
      this.preferVerticalSplitscreen = this.register("prefer_vertical_splitscreen", false, Codec.BOOL);
      this.audioMethod = this.register("audio_method", AudioMethod.CLOSEST_ORIGIN, AudioMethod.CODEC);
      this.musicMethod = this.register("music_method", MusicMethod.FIRST_PLAYER, MusicMethod.CODEC);
      this.serverConfig = this.register("server", SplitscreenServerConfig.INSTANCE);
   }

   public SplitscreenServerSharedConfig createSharedConfig() {
      return new SplitscreenServerSharedConfig((AudioMethod)this.audioMethod.get());
   }

   public void saveToFile() {
      super.saveToFile();
      ConfigSyncRegistry.onSave(CONFIG_ID);
   }
}
