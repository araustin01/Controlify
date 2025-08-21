package dev.isxander.controlify.mixins.feature.screenop.vanilla;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ComponentProcessorProvider;
import dev.isxander.controlify.screenop.compat.vanilla.SliderComponentProcessor;
import net.minecraft.class_357;
import net.minecraft.class_8015;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_357.class})
public class AbstractSliderButtonMixin implements ComponentProcessorProvider {
   @Shadow
   private boolean field_41796;
   @Unique
   private final SliderComponentProcessor controlify$processor = new SliderComponentProcessor((class_357)this, () -> {
      return this.field_41796;
   }, (val) -> {
      this.field_41796 = val;
   });

   @ModifyExpressionValue(
      method = {"method_25365(Z)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_310;method_48186()Lnet/minecraft/class_8015;"
)}
   )
   private class_8015 shouldChangeValue(class_8015 type) {
      return Controlify.instance().currentInputMode().isController() ? class_8015.field_41777 : type;
   }

   public ComponentProcessor componentProcessor() {
      return this.controlify$processor;
   }
}
