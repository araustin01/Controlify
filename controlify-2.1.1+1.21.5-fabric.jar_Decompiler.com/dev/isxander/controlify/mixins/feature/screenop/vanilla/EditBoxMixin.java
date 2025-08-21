package dev.isxander.controlify.mixins.feature.screenop.vanilla;

import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ComponentProcessorProvider;
import dev.isxander.controlify.screenop.compat.vanilla.EditBoxComponentProcessor;
import net.minecraft.class_342;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({class_342.class})
public class EditBoxMixin implements ComponentProcessorProvider {
   @Unique
   private final ComponentProcessor processor = new EditBoxComponentProcessor();

   public ComponentProcessor componentProcessor() {
      return this.processor;
   }
}
