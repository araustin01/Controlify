package dev.isxander.controlify.controller;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ControllerUID(@NotNull String string) {
   public ControllerUID(@NotNull String string) {
      this.string = string;
   }

   @Nullable
   public static ControllerUID fromNullableString(@Nullable String value) {
      return value == null ? null : new ControllerUID(value);
   }

   @Nullable
   public static String toNullableString(@Nullable ControllerUID uid) {
      return uid == null ? null : uid.string();
   }

   @NotNull
   public String string() {
      return this.string;
   }
}
