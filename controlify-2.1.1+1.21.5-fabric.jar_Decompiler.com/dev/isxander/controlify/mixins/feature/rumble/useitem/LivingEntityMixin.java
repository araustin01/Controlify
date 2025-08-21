package dev.isxander.controlify.mixins.feature.rumble.useitem;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.class_1268;
import net.minecraft.class_1309;
import net.minecraft.class_1799;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1309.class})
public abstract class LivingEntityMixin {
   @Inject(
      method = {"method_6019(Lnet/minecraft/class_1268;)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_1799;method_7935(Lnet/minecraft/class_1309;)I"
)}
   )
   protected void onStartUsingItem(class_1268 hand, CallbackInfo ci, @Local class_1799 stack) {
   }

   @Inject(
      method = {"method_6021()V"},
      at = {@At("HEAD")}
   )
   protected void onStopUsingItem(CallbackInfo ci) {
   }

   @Inject(
      method = {"method_37119(Lnet/minecraft/class_1799;)V"},
      at = {@At("HEAD")}
   )
   protected void onUpdateUsingItem(class_1799 stack, CallbackInfo ci) {
   }
}
