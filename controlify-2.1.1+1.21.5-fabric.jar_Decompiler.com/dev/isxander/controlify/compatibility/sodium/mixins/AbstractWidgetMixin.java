package dev.isxander.controlify.compatibility.sodium.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.controlify.Controlify;
import net.caffeinemc.mods.sodium.client.gui.widgets.AbstractWidget;
import net.minecraft.class_8015;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({AbstractWidget.class})
public class AbstractWidgetMixin {
   @ModifyExpressionValue(
      method = {"method_25365(Z)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_310;method_48186()Lnet/minecraft/class_8015;"
)}
   )
   private class_8015 forceSetFocusedOnController(class_8015 type) {
      return Controlify.instance().currentInputMode().isController() ? class_8015.field_43097 : type;
   }
}
