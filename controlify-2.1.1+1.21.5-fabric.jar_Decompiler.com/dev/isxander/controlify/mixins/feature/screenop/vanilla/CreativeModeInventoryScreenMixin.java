package dev.isxander.controlify.mixins.feature.screenop.vanilla;

import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.screenop.ScreenProcessorProvider;
import dev.isxander.controlify.screenop.compat.vanilla.CreativeModeInventoryScreenProcessor;
import net.minecraft.class_481;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({class_481.class})
public abstract class CreativeModeInventoryScreenMixin extends AbstractContainerScreenMixin implements ScreenProcessorProvider {
   @Unique
   protected CreativeModeInventoryScreenProcessor screenProcessor = new CreativeModeInventoryScreenProcessor((class_481)this, () -> {
      return this.field_2787;
   }, this::method_2383, this::handleControllerItemSlotActions);

   public ScreenProcessor<?> screenProcessor() {
      return this.screenProcessor;
   }
}
