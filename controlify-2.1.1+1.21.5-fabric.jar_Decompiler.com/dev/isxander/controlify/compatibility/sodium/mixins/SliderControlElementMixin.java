package dev.isxander.controlify.compatibility.sodium.mixins;

import dev.isxander.controlify.compatibility.sodium.screenop.SliderControlProcessor;
import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ComponentProcessorProvider;
import net.caffeinemc.mods.sodium.client.gui.options.Option;
import net.caffeinemc.mods.sodium.client.gui.options.control.ControlElement;
import net.caffeinemc.mods.sodium.client.util.Dim2i;
import net.minecraft.class_3532;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(
   targets = {"net/caffeinemc/mods/sodium/client/gui/options/control/SliderControl$Button"}
)
public abstract class SliderControlElementMixin extends ControlElement<Integer> implements ComponentProcessorProvider {
   @Shadow
   @Final
   private int interval;
   @Shadow
   @Final
   private int min;
   @Shadow
   @Final
   private int max;
   @Unique
   private final ComponentProcessor controlify$componentProcessor = new SliderControlProcessor(this::incrementSlider);

   public SliderControlElementMixin(Option<Integer> option, Dim2i dim) {
      super(option, dim);
   }

   public ComponentProcessor componentProcessor() {
      return this.controlify$componentProcessor;
   }

   @Unique
   private void incrementSlider(boolean reverse) {
      this.option.setValue(class_3532.method_15340((Integer)this.option.getValue() + (reverse ? -this.interval : this.interval), this.min, this.max));
   }
}
