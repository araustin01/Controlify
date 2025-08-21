package dev.isxander.controlify.mixins.feature.bind;

import dev.isxander.controlify.gui.screen.RadialMenuScreen;
import net.minecraft.class_310;
import net.minecraft.class_329;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_329.class})
public class GuiMixin {
   @Shadow
   @Final
   private class_310 field_2035;

   @Inject(
      method = {"method_1736(Lnet/minecraft/class_332;Lnet/minecraft/class_9779;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void shouldRenderCrosshair(CallbackInfo ci) {
      if (this.field_2035.field_1755 instanceof RadialMenuScreen) {
         ci.cancel();
      }

   }
}
