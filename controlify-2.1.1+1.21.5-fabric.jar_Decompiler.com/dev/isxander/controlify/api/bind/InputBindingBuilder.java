package dev.isxander.controlify.api.bind;

import dev.isxander.controlify.bindings.BindContext;
import dev.isxander.controlify.bindings.input.Input;
import dev.isxander.controlify.controller.ControllerEntity;
import java.util.function.Function;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_304;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface InputBindingBuilder {
   InputBindingBuilder id(@NotNull class_2960 var1);

   InputBindingBuilder id(@NotNull String var1, @NotNull String var2);

   InputBindingBuilder name(@NotNull class_2561 var1);

   InputBindingBuilder description(@NotNull class_2561 var1);

   InputBindingBuilder category(@NotNull class_2561 var1);

   InputBindingBuilder defaultInput(@Nullable Input var1);

   InputBindingBuilder allowedContexts(@NotNull @Nullable BindContext... var1);

   InputBindingBuilder radialCandidate(@Nullable class_2960 var1);

   InputBindingBuilder addKeyCorrelation(@NotNull class_304 var1);

   InputBindingBuilder keyEmulation(@NotNull class_304 var1);

   InputBindingBuilder keyEmulation(@NotNull class_304 var1, @Nullable Function<ControllerEntity, Boolean> var2);
}
