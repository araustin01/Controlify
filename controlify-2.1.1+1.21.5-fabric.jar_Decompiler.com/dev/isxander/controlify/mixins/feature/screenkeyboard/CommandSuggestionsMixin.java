package dev.isxander.controlify.mixins.feature.screenkeyboard;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.controlify.screenkeyboard.ChatKeyboardDucky;
import net.minecraft.class_310;
import net.minecraft.class_408;
import net.minecraft.class_437;
import net.minecraft.class_4717;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_4717.class})
public class CommandSuggestionsMixin {
   @Shadow
   @Final
   class_310 field_21597;

   @ModifyExpressionValue(
      method = {"method_44932(Lnet/minecraft/class_332;)V", "method_23920(Z)V"},
      at = {@At(
   value = "FIELD",
   target = "Lnet/minecraft/class_437;field_22790:I"
)}
   )
   private int modifyUsageHeight(int height) {
      class_437 var3 = this.field_21597.field_1755;
      if (var3 instanceof class_408) {
         class_408 chat = (class_408)var3;
         return (int)((float)height * (1.0F - ChatKeyboardDucky.getKeyboardShiftAmount(chat)));
      } else {
         return height;
      }
   }
}
