package dev.isxander.splitscreen.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record SplitscreenServerSharedConfig(AudioMethod audioMethod) {
   public static final Codec<SplitscreenServerSharedConfig> CODEC = RecordCodecBuilder.create((instance) -> {
      return instance.group(AudioMethod.CODEC.fieldOf("audio_method").forGetter((cfg) -> {
         return cfg.audioMethod;
      })).apply(instance, SplitscreenServerSharedConfig::new);
   });

   public SplitscreenServerSharedConfig(AudioMethod audioMethod) {
      this.audioMethod = audioMethod;
   }

   public AudioMethod audioMethod() {
      return this.audioMethod;
   }
}
