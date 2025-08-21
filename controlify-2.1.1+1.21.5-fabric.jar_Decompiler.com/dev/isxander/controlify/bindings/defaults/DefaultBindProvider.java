package dev.isxander.controlify.bindings.defaults;

import dev.isxander.controlify.bindings.input.Input;
import net.minecraft.class_2960;
import org.jetbrains.annotations.Nullable;

public interface DefaultBindProvider {
   DefaultBindProvider EMPTY = (bind) -> {
      return null;
   };

   @Nullable
   Input getDefaultBind(class_2960 var1);
}
