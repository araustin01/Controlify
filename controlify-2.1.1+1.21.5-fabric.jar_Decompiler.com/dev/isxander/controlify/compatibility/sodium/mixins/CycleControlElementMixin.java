package dev.isxander.controlify.compatibility.sodium.mixins;

import dev.isxander.controlify.compatibility.sodium.screenop.CycleControlProcessor;
import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ComponentProcessorProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(
   targets = {"net/caffeinemc/mods/sodium/client/gui/options/control/CyclingControl$CyclingControlElement"}
)
public abstract class CycleControlElementMixin implements ComponentProcessorProvider {
   @Unique
   private final ComponentProcessor controlify$componentProcessor = new CycleControlProcessor(this::cycleControl);

   @Shadow
   public abstract void cycleControl(boolean var1);

   public ComponentProcessor componentProcessor() {
      return this.controlify$componentProcessor;
   }
}
