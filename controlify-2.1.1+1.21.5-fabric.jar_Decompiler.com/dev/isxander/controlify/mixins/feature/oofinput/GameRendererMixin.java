package dev.isxander.controlify.mixins.feature.oofinput;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.controlify.Controlify;
import net.minecraft.class_757;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_757.class})
public class GameRendererMixin {
   @ModifyExpressionValue(
      method = {"method_3192(Lnet/minecraft/class_9779;Z)V"},
      at = {@At(
   value = "FIELD",
   target = "Lnet/minecraft/class_315;field_1837:Z",
   opcode = 180
)}
   )
   private boolean shouldPauseOnLossFocus(boolean original) {
      return original && (!Controlify.instance().config().globalSettings().outOfFocusInput || !Controlify.instance().getCurrentController().isPresent());
   }
}
