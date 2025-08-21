package dev.isxander.controlify.mixins.feature.screenop.vanilla;

import dev.isxander.controlify.screenop.CustomFocus;
import net.minecraft.class_362;
import net.minecraft.class_364;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({class_362.class})
public abstract class AbstractContainerEventHandlerMixin implements CustomFocus {
   @Shadow
   @Nullable
   public abstract class_364 method_25399();

   public class_364 getCustomFocus() {
      return this.method_25399();
   }
}
