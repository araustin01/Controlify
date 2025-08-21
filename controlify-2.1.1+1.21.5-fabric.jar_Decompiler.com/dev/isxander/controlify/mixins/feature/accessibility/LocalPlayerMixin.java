package dev.isxander.controlify.mixins.feature.accessibility;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.controller.GenericControllerConfig;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_746.class})
public class LocalPlayerMixin {
   @ModifyExpressionValue(
      method = {"method_3136()V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_7172;method_41753()Ljava/lang/Object;"
)}
   )
   private Object shouldUseAutoJump(Object keyboardAutoJump) {
      return ControlifyApi.get().currentInputMode().isController() ? ControlifyApi.get().getCurrentController().map((controller) -> {
         return ((GenericControllerConfig)controller.genericConfig().config()).autoJump;
      }).orElse(false) : keyboardAutoJump;
   }
}
