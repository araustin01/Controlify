package dev.isxander.controlify.mixins.feature.font;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.controlify.api.ControlifyApi;
import net.minecraft.class_2588;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_2588.class})
public class TranslatableContentsMixin {
   @ModifyExpressionValue(
      method = {"method_11025()V"},
      at = {@At(
   value = "FIELD",
   target = "Lnet/minecraft/class_2588;field_11876:Ljava/lang/String;"
)}
   )
   private String replaceControllerActiveKey(String originalKey) {
      if ("controlify.placeholder.controller_active".equals(originalKey)) {
         return ControlifyApi.get().currentInputMode().isController() ? "controlify.placeholder" : originalKey;
      } else {
         return originalKey;
      }
   }
}
