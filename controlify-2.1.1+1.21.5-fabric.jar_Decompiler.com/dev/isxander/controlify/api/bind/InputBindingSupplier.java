package dev.isxander.controlify.api.bind;

import dev.isxander.controlify.controller.ControllerEntity;
import java.util.Objects;
import net.minecraft.class_2960;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface InputBindingSupplier {
   default InputBinding on(@NotNull ControllerEntity controller) {
      return (InputBinding)Objects.requireNonNull(this.onOrNull(controller), () -> {
         String var10000 = String.valueOf(this.bindId());
         return "Attempted to fetch " + var10000 + " for controller " + String.valueOf(controller.uid()) + " but it did not exist.The binding registry callback may have a filter that did not pass for this controller.";
      });
   }

   @Nullable
   InputBinding onOrNull(ControllerEntity var1);

   class_2960 bindId();
}
