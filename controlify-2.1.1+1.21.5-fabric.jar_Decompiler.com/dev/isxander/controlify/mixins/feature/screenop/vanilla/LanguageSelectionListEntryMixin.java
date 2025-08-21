package dev.isxander.controlify.mixins.feature.screenop.vanilla;

import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ComponentProcessorProvider;
import dev.isxander.controlify.screenop.compat.vanilla.LanguageSelectionListComponentProcessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(
   targets = {"net/minecraft/class_426$class_4195$class_4194"}
)
public class LanguageSelectionListEntryMixin implements ComponentProcessorProvider {
   @Shadow
   @Final
   String field_41846;
   @Unique
   private LanguageSelectionListComponentProcessor controlify$componentProcessor = null;

   public ComponentProcessor componentProcessor() {
      if (this.controlify$componentProcessor == null) {
         this.controlify$componentProcessor = new LanguageSelectionListComponentProcessor(this.field_41846);
      }

      return this.controlify$componentProcessor;
   }
}
