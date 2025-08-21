package dev.isxander.controlify.mixins.feature.rumble.waterland;

import net.minecraft.class_1299;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1937;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1657.class})
public abstract class PlayerMixin extends class_1309 {
   @Shadow
   public abstract boolean method_7325();

   protected PlayerMixin(class_1299<? extends class_1309> entityType, class_1937 level) {
      super(entityType, level);
   }

   @Inject(
      method = {"method_5746()V"},
      at = {@At("HEAD")}
   )
   protected void splashRumble(CallbackInfo ci) {
   }
}
