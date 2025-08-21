package dev.isxander.controlify.mixins.feature.screenop.vanilla;

import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ComponentProcessorProvider;
import dev.isxander.controlify.screenop.compat.vanilla.AbstractButtonComponentProcessor;
import net.minecraft.class_4264;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({class_4264.class})
public class AbstractButtonMixin implements ComponentProcessorProvider {
   @Unique
   private final AbstractButtonComponentProcessor controlify$processor = new AbstractButtonComponentProcessor((class_4264)this);

   public ComponentProcessor componentProcessor() {
      return this.controlify$processor;
   }
}
