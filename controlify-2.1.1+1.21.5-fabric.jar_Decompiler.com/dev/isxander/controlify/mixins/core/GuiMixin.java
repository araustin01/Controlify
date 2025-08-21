package dev.isxander.controlify.mixins.core;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.ingame.InGameInputHandler;
import net.minecraft.class_329;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_329.class})
public class GuiMixin {
   @ModifyExpressionValue(
      method = {"method_55804(Lnet/minecraft/class_332;Lnet/minecraft/class_9779;)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_304;method_1434()Z"
)}
   )
   private boolean shouldShowPlayerList(boolean keyDown) {
      return keyDown || (Boolean)Controlify.instance().inGameInputHandler().map(InGameInputHandler::shouldShowPlayerList).orElse(false);
   }
}
