package dev.isxander.controlify.mixins.feature.virtualmouse;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.virtualmouse.VirtualMouseHandler;
import java.util.Optional;
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
   private void onScreenChanged(class_437 screen, CallbackInfo ci) {
      Optional.ofNullable(Controlify.instance().virtualMouseHandler()).ifPresent(VirtualMouseHandler::onScreenChanged);
   }

   @Inject(
      method = {"method_1523(Z)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_312;method_55793()V"
)}
   )
   private void onUpdateMouse(boolean tick, CallbackInfo ci) {
      Optional.ofNullable(Controlify.instance().virtualMouseHandler()).ifPresent(VirtualMouseHandler::updateMouse);
   }
}
