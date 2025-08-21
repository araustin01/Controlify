package dev.isxander.controlify.mixins.feature.rumble.explosion;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.rumble.BasicRumbleEffect;
import dev.isxander.controlify.rumble.RumbleSource;
import dev.isxander.controlify.rumble.RumbleState;
import net.minecraft.class_1538;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_1538.class})
public class LightningBoltMixin {
   @ModifyExpressionValue(
      method = {"method_5773()V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_1937;method_8608()Z"
)}
   )
   private boolean onLightningStrike(boolean client) {
      if (client) {
         ControlifyApi.get().getCurrentController().flatMap(ControllerEntity::rumble).ifPresent((controller) -> {
            controller.rumbleManager().play(RumbleSource.WORLD, BasicRumbleEffect.join(BasicRumbleEffect.constant(1.0F, 0.2F, 6), BasicRumbleEffect.byTime((t) -> {
               return new RumbleState(0.0F, 1.0F - t * 0.2F);
            }, 10)));
         });
      }

      return client;
   }
}
