package dev.isxander.controlify.mixins.feature.screenop.vanilla;

import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.screenop.ScreenProcessorProvider;
import dev.isxander.controlify.screenop.compat.vanilla.PauseScreenProcessor;
import net.minecraft.class_4185;
import net.minecraft.class_433;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin({class_433.class})
public class PauseScreenMixin implements ScreenProcessorProvider {
   @Shadow
   @Nullable
   private class_4185 field_40792;
   @Unique
   private final PauseScreenProcessor processor = new PauseScreenProcessor((class_433)this, () -> {
      return this.field_40792;
   });

   public ScreenProcessor<?> screenProcessor() {
      return this.processor;
   }
}
