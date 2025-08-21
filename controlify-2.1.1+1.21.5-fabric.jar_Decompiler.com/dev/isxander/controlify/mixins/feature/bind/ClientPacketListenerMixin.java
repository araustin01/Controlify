package dev.isxander.controlify.mixins.feature.bind;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.bindings.ControlifyBindings;
import java.util.Optional;
import net.minecraft.class_2561;
import net.minecraft.class_634;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_634.class})
public class ClientPacketListenerMixin {
   @ModifyExpressionValue(
      method = {"method_11080(Lnet/minecraft/class_2752;)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_304;method_16007()Lnet/minecraft/class_2561;"
)}
   )
   private class_2561 useControllerTextForSneakTip(class_2561 original) {
      return (class_2561)ControlifyApi.get().getCurrentController().flatMap((c) -> {
         return ControlifyApi.get().currentInputMode().isController() ? Optional.of(c) : Optional.empty();
      }).flatMap((c) -> {
         return Optional.ofNullable(ControlifyBindings.SNEAK.on(c));
      }).map(InputBinding::inputIcon).orElse(original);
   }
}
