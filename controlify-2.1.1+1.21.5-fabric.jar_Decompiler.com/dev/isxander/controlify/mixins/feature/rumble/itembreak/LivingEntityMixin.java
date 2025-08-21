package dev.isxander.controlify.mixins.feature.rumble.itembreak;

import net.minecraft.class_1309;
import net.minecraft.class_1799;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1309.class})
public class LivingEntityMixin {
   @Inject(
      method = {"method_6045(Lnet/minecraft/class_1799;)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_1309;method_6037(Lnet/minecraft/class_1799;I)V"
)}
   )
   protected void onBreakItemParticles(class_1799 stack, CallbackInfo ci) {
   }
}
