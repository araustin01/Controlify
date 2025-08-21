package dev.isxander.splitscreen.config;

import com.mojang.serialization.Codec;
import net.minecraft.class_3542;
import org.jetbrains.annotations.NotNull;

public enum AudioMethod implements class_3542 {
   NOTHING("nothing"),
   FIRST_PLAYER_ONLY("first_player_only"),
   CLOSEST_ORIGIN("closest_origin"),
   CONCERNING_PLAYER("concerning_player"),
   UNIQUE_CHANNEL("unique_channel"),
   SEPARATE_OUTPUTS("separate_outputs");

   public static final AudioMethod[] SUPPORTING_SERVER_REQUIRED_METHODS = new AudioMethod[]{CLOSEST_ORIGIN, CONCERNING_PLAYER};
   public static final Codec<AudioMethod> CODEC = class_3542.method_28140(AudioMethod::values);
   private final String identifier;

   private AudioMethod(String identifier) {
      this.identifier = identifier;
   }

   @NotNull
   public String method_15434() {
      return this.identifier;
   }

   // $FF: synthetic method
   private static AudioMethod[] $values() {
      return new AudioMethod[]{NOTHING, FIRST_PLAYER_ONLY, CLOSEST_ORIGIN, CONCERNING_PLAYER, UNIQUE_CHANNEL, SEPARATE_OUTPUTS};
   }
}
