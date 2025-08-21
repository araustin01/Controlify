package dev.isxander.controlify.mixins.feature.rumble.damage;

import net.minecraft.class_1657;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1657.class})
public class PlayerMixin extends LivingEntityMixin {
   @Inject(
      method = {"method_5879(F)V"},
      at = {@At("HEAD")}
   )
   protected void onEntityHurtMeDamage(float yaw, CallbackInfo ci) {
   }
}
