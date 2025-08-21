package dev.isxander.controlify.mixins.feature.screenop.vanilla;

import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.screenop.ScreenProcessorProvider;
import dev.isxander.controlify.screenop.compat.vanilla.TitleScreenProcessor;
import net.minecraft.class_442;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({class_442.class})
public class TitleScreenMixin implements ScreenProcessorProvider {
   @Unique
   private final ScreenProcessor<?> processor = new TitleScreenProcessor((class_442)this);

   public ScreenProcessor<?> screenProcessor() {
      return this.processor;
   }
}
