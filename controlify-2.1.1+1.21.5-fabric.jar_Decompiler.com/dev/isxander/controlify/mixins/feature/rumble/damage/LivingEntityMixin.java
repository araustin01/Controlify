package dev.isxander.controlify.mixins.feature.rumble.damage;

import net.minecraft.class_1282;
import net.minecraft.class_1309;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1309.class})
public class LivingEntityMixin {
   @Shadow
   public int field_6254;
   @Shadow
   public int field_6235;

   @Inject(
      method = {"method_48922(Lnet/minecraft/class_1282;)V"},
      at = {@At("HEAD")}
   )
   protected void onHealthChangedDamage(class_1282 source, CallbackInfo ci) {
   }
}
