package dev.isxander.controlify.api;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.InputMode;
import dev.isxander.controlify.controller.ControllerEntity;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public interface ControlifyApi {
   @NotNull
   Optional<ControllerEntity> getCurrentController();

   @NotNull
   InputMode currentInputMode();

   boolean setInputMode(@NotNull InputMode var1);

   static ControlifyApi get() {
      return Controlify.instance();
   }
}
