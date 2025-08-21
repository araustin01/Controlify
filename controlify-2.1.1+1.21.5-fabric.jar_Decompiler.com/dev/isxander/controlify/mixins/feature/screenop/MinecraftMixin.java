package dev.isxander.controlify.mixins.feature.screenop;

import dev.isxander.controlify.screenop.ComponentProcessorProvider;
import net.minecraft.class_310;
import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_310.class})
public class MinecraftMixin {
   @Inject(
      method = {"method_1507(Lnet/minecraft/class_437;)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_310;method_24288()V"
)}
   )
   private void changeScreen(class_437 screen, CallbackInfo ci) {
      ComponentProcessorProvider.REGISTRY.clearCache();
   }
}
