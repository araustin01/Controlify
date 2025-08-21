package dev.isxander.controlify.mixins.feature.screenop.vanilla;

import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.screenop.ScreenProcessorProvider;
import dev.isxander.controlify.screenop.compat.vanilla.JoinMultiplayerScreenProcessor;
import net.minecraft.class_4267;
import net.minecraft.class_500;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin({class_500.class})
public class JoinMultiplayerScreenMixin implements ScreenProcessorProvider {
   @Shadow
   protected class_4267 field_3043;
   @Unique
   private final JoinMultiplayerScreenProcessor controlify$processor = new JoinMultiplayerScreenProcessor((class_500)this, () -> {
      return this.field_3043;
   });

   public ScreenProcessor<?> screenProcessor() {
      return this.controlify$processor;
   }
}
