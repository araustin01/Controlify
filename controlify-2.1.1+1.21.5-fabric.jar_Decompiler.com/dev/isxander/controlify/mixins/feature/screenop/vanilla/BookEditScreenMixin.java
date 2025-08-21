package dev.isxander.controlify.mixins.feature.screenop.vanilla;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.isxander.controlify.Controlify;
import net.minecraft.class_364;
import net.minecraft.class_473;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_473.class})
public class BookEditScreenMixin {
   @WrapWithCondition(
      method = {"method_25394(Lnet/minecraft/class_332;IIF)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_473;method_25395(Lnet/minecraft/class_364;)V"
)}
   )
   private boolean shouldRemoveFocus(class_473 instance, class_364 guiEventListener) {
      return !Controlify.instance().currentInputMode().isController();
   }
}
