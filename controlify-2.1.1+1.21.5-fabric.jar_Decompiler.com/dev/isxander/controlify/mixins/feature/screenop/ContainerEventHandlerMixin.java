package dev.isxander.controlify.mixins.feature.screenop;

import dev.isxander.controlify.screenop.CustomFocus;
import net.minecraft.class_364;
import net.minecraft.class_4069;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({class_4069.class})
public interface ContainerEventHandlerMixin extends CustomFocus {
   @Shadow
   @Nullable
   class_364 method_25399();

   default class_364 getCustomFocus() {
      return this.method_25399();
   }
}
