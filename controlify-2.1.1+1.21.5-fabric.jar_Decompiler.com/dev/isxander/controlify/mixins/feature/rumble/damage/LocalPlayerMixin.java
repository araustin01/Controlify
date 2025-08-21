package dev.isxander.controlify.mixins.feature.rumble.damage;

import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.rumble.BasicRumbleEffect;
import dev.isxander.controlify.rumble.RumbleSource;
import net.minecraft.class_1282;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_746.class})
public abstract class LocalPlayerMixin extends PlayerMixin {
   protected void onHealthChangedDamage(class_1282 source, CallbackInfo ci) {
      if (this.field_6235 < 10) {
         this.doDamageRumble();
      }

   }

   protected void onEntityHurtMeDamage(float yaw, CallbackInfo ci) {
      if (this.field_6235 < 10) {
         this.doDamageRumble();
      }

   }

   @Unique
   private void doDamageRumble() {
      ControlifyApi.get().getCurrentController().flatMap(ControllerEntity::rumble).ifPresent((rumble) -> {
         rumble.rumbleManager().play(RumbleSource.PLAYER, BasicRumbleEffect.constant(0.8F, 0.5F, 5));
      });
   }
}
