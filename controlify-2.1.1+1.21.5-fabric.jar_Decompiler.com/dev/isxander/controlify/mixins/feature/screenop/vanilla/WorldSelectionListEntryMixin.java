package dev.isxander.controlify.mixins.feature.screenop.vanilla;

import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ComponentProcessorProvider;
import dev.isxander.controlify.screenop.compat.vanilla.WorldListEntryComponentProcessor;
import net.minecraft.class_528.class_4272;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({class_4272.class})
public class WorldSelectionListEntryMixin implements ComponentProcessorProvider {
   @Unique
   private final WorldListEntryComponentProcessor controlify$processor = new WorldListEntryComponentProcessor();

   public ComponentProcessor componentProcessor() {
      return this.controlify$processor;
   }
}
