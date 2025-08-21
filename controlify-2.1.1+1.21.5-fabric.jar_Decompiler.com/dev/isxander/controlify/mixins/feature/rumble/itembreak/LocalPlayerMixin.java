package dev.isxander.controlify.mixins.feature.rumble.itembreak;

import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.rumble.BasicRumbleEffect;
import dev.isxander.controlify.rumble.RumbleSource;
import dev.isxander.controlify.rumble.RumbleState;
import net.minecraft.class_1799;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_746.class})
public class LocalPlayerMixin extends LivingEntityMixin {
   protected void onBreakItemParticles(class_1799 stack, CallbackInfo ci) {
      ControlifyApi.get().getCurrentController().flatMap(ControllerEntity::rumble).ifPresent((controller) -> {
         controller.rumbleManager().play(RumbleSource.PLAYER, BasicRumbleEffect.byTick((tick) -> {
            return new RumbleState(tick <= 4 ? 1.0F : 0.0F, 1.0F);
         }, 10));
      });
   }
}
