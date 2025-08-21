package dev.isxander.controlify.mixins.feature.screenop.vanilla;

import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ComponentProcessorProvider;
import dev.isxander.controlify.screenop.compat.vanilla.ServerSelectionListEntryComponentProcessor;
import net.minecraft.class_4267.class_4268;
import net.minecraft.class_4267.class_504;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({class_504.class})
public class ServerSelectionListEntryMixin implements ComponentProcessorProvider {
   @Unique
   private final ServerSelectionListEntryComponentProcessor controlify$componentProcessor = new ServerSelectionListEntryComponentProcessor();

   public ComponentProcessor componentProcessor() {
      return (ComponentProcessor)((class_504)this instanceof class_4268 ? ComponentProcessor.EMPTY : this.controlify$componentProcessor);
   }
}
