package dev.isxander.controlify.mixins.feature.virtualmouse;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.InputMode;
import net.minecraft.class_312;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_312.class})
public class MouseHandlerMixin {
   @WrapWithCondition(
      method = {"method_1610()V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_3675;method_15984(JIDD)V"
)}
   )
   private boolean shouldReleaseMouse(long window, int newMouseState, double x, double y) {
      return Controlify.instance().currentInputMode() != InputMode.CONTROLLER;
   }
}
