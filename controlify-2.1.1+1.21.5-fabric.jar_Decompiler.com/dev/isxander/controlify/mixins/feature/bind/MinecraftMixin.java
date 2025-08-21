package dev.isxander.controlify.mixins.feature.bind;

import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.input.InputComponent;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_310.class})
public class MinecraftMixin {
   @Inject(
      method = {"method_1507(Lnet/minecraft/class_437;)V"},
      at = {@At("HEAD")}
   )
   private void notifyBindGuiOutputOfScreenChange(CallbackInfo ci) {
      ControlifyApi.get().getCurrentController().flatMap(ControllerEntity::input).ifPresent(InputComponent::notifyGuiPressOutputsOfNavigate);
   }
}
