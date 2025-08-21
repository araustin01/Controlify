package dev.isxander.controlify.mixins.feature.reacharound;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.controlify.reacharound.ReachAroundHandler;
import net.minecraft.class_239;
import net.minecraft.class_310;
import net.minecraft.class_757;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_757.class})
public class GameRendererMixin {
   @Shadow
   @Final
   private class_310 field_4015;

   @ModifyExpressionValue(
      method = {"method_56153(Lnet/minecraft/class_1297;DDF)Lnet/minecraft/class_239;"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_1297;method_5745(DFZ)Lnet/minecraft/class_239;"
)}
   )
   private class_239 modifyPick(class_239 hitResult) {
      return ReachAroundHandler.getReachAroundHitResult(this.field_4015.method_1560(), hitResult);
   }
}
