package dev.isxander.splitscreen.server.mixins.status;

import dev.isxander.splitscreen.client.SplitscreenBootstrapper;
import dev.isxander.splitscreen.server.status.ServerStatusSplitscreenExt;
import net.minecraft.class_1921;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_500;
import net.minecraft.class_642;
import net.minecraft.class_4267.class_4270;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_4270.class})
public class ServerSelectionList$OnlineServerEntryMixin {
   @Shadow
   @Final
   private class_642 field_19120;
   @Shadow
   @Final
   private class_500 field_19118;

   @Inject(
      method = {"method_25343(Lnet/minecraft/class_332;IIIIIIIZF)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_642;method_49306()[B"
)}
   )
   private void renderSplitscreenStatusIcon(class_332 guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick, CallbackInfo ci) {
      SplitscreenBootstrapper.getController().ifPresent((controller) -> {
         ServerStatusSplitscreenExt ext = (ServerStatusSplitscreenExt)ServerStatusSplitscreenExt.getExt(this.field_19120).orElse((Object)null);
         if (ext != null && ext.supportedProtocols().length != 0) {
            int x = left + width - 5 - 10 - 10 - 2;
            boolean supported = controller.getPawnCount(false) <= ext.maxSubPlayers();
            class_2960 sprite = supported ? ServerStatusSplitscreenExt.SPLITSCREEN_SUPPORTED_SPRITE : ServerStatusSplitscreenExt.SPLITSCREEN_UNSUPPORTED_SPRITE;
            guiGraphics.method_52706(class_1921::method_62277, sprite, x, top, 10, 8);
            if (mouseX >= x && mouseX <= x + 10 && mouseY >= top && mouseY <= top + 8) {
               class_2561 tooltip = supported ? class_2561.method_43471("controlify.splitscreen.tooltip.supported") : class_2561.method_43469("controlify.splitscreen.tooltip.unsupported", new Object[]{ext.maxSubPlayers() + 1});
               this.field_19118.method_47415(tooltip);
            }

         }
      });
   }

   @ModifyVariable(
      method = {"method_25343(Lnet/minecraft/class_332;IIIIIIIZF)V"},
      at = @At(
   value = "STORE",
   ordinal = 0
),
      ordinal = 9
   )
   private int modifyPlayerCountX(int x) {
      ServerStatusSplitscreenExt ext = (ServerStatusSplitscreenExt)ServerStatusSplitscreenExt.getExt(this.field_19120).orElse((Object)null);
      return ext != null && ext.supportedProtocols().length != 0 ? x - 10 - 2 : x;
   }
}
