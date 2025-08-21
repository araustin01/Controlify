package dev.isxander.splitscreen.config;

import com.mojang.serialization.Codec;
import net.minecraft.class_3542;
import org.jetbrains.annotations.NotNull;

public enum MusicMethod implements class_3542 {
   FIRST_PLAYER("first_player"),
   PRIORITY("priority");

   public static final Codec<MusicMethod> CODEC = class_3542.method_28140(MusicMethod::values);
   private final String name;

   private MusicMethod(String name) {
      this.name = name;
   }

   @NotNull
   public String method_15434() {
      return this.name;
   }

   // $FF: synthetic method
   private static MusicMethod[] $values() {
      return new MusicMethod[]{FIRST_PLAYER, PRIORITY};
   }
}
