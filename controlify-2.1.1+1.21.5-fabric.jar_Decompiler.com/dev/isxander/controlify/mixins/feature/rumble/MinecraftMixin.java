package dev.isxander.controlify.mixins.feature.rumble;

import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.controller.ControllerEntity;
import net.minecraft.class_310;
import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_310.class})
public class MinecraftMixin {
   @Inject(
      method = {"method_56134(Lnet/minecraft/class_437;)V"},
      at = {@At("HEAD")}
   )
   private void clearRumbleEffects(class_437 disconnectScreen, CallbackInfo ci) {
      ControlifyApi.get().getCurrentController().flatMap(ControllerEntity::rumble).ifPresent((controller) -> {
         controller.rumbleManager().clearEffects();
      });
   }
}
