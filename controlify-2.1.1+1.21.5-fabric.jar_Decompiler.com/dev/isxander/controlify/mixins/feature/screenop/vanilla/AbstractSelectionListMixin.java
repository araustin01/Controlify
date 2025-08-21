package dev.isxander.controlify.mixins.feature.screenop.vanilla;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.controlify.Controlify;
import net.minecraft.class_350;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_350.class})
public class AbstractSelectionListMixin {
   @ModifyExpressionValue(
      method = {"method_25395(Lnet/minecraft/class_364;)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_8015;method_48183()Z"
)}
   )
   private boolean shouldEnsureEntryVisible(boolean keyboard) {
      return keyboard || Controlify.instance().currentInputMode().isController();
   }
}
