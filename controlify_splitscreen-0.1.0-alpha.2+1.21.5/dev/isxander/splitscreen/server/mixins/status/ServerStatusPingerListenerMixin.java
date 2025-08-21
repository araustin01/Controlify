package dev.isxander.splitscreen.server.mixins.status;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.splitscreen.server.status.ServerStatusSplitscreenExt;
import net.minecraft.class_2926;
import net.minecraft.class_642;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(
   targets = {"net/minecraft/class_644$1"}
)
public class ServerStatusPingerListenerMixin {
   @Shadow
   @Final
   class_642 field_3776;

   @ModifyExpressionValue(
      method = {"method_12667(Lnet/minecraft/class_2924;)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_2924;comp_1272()Lnet/minecraft/class_2926;"
)}
   )
   private class_2926 addSplitscreenInfoToServerData(class_2926 status) {
      ServerStatusSplitscreenExt.getExt(status).ifPresent((ext) -> {
         ServerStatusSplitscreenExt.setExt(this.field_3776, ext);
      });
      return status;
   }
}
