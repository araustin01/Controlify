package dev.isxander.splitscreen.client.mixins.core;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Definitions;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.expression.Expressions;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.splitscreen.client.SplitscreenBootstrapper;
import dev.isxander.splitscreen.client.features.screenop.ScreenSplitscreenMode;
import dev.isxander.splitscreen.client.features.screenop.ScreenSplitscreenModeRegistry;
import dev.isxander.splitscreen.client.host.SplitscreenController;
import java.util.Optional;
import net.minecraft.class_1132;
import net.minecraft.class_310;
import net.minecraft.class_3682;
import net.minecraft.class_437;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_310.class})
public abstract class MinecraftMixin {
   @Shadow
   @Nullable
   public class_437 field_1755;
   @Shadow
   @Nullable
   private class_1132 field_1766;
   @Shadow
   @Final
   private class_3682 field_1686;

   @Shadow
   protected abstract String method_24287();

   @ModifyExpressionValue(
      method = {"method_1523(Z)V"},
      slice = {@Slice(
   to = @At(
   value = "MIXINEXTRAS:EXPRESSION",
   id = "slice_end"
)
)},
      at = {@At(
   value = "MIXINEXTRAS:EXPRESSION:LAST",
   id = "target"
)}
   )
   @Definitions({@Definition(
   id = "pause",
   field = {"Lnet/minecraft/class_310;field_1734:Z"}
), @Definition(
   id = "localServer",
   field = {"Lnet/minecraft/class_310;field_1766:Lnet/minecraft/class_1132;"}
), @Definition(
   id = "isPublished",
   method = {"Lnet/minecraft/class_1132;method_3860()Z"}
)})
   @Expressions({@Expression(
   value = {"this.pause = ?"},
   id = "slice_end"
), @Expression(
   value = {"this.localServer.isPublished()"},
   id = "target"
)})
   private boolean shouldTickServerInPausableScreen(boolean isLANServer) {
      if (!isLANServer) {
         return false;
      } else {
         Optional<SplitscreenController> controllerOpt = SplitscreenBootstrapper.getController();
         if (controllerOpt.isEmpty()) {
            return true;
         } else {
            SplitscreenController controller = (SplitscreenController)controllerOpt.get();
            int playerCount = this.field_1766.method_3788();
            int pawnCount = controller.getPawnCount(true);
            boolean localOnlyLanServer = playerCount == pawnCount;
            boolean isFullscreen = ScreenSplitscreenModeRegistry.getMode(this.field_1755) == ScreenSplitscreenMode.FULLSCREEN || playerCount == 1;
            return localOnlyLanServer && !isFullscreen;
         }
      }
   }

   @Inject(
      method = {"method_51736(Lnet/minecraft/class_310$class_8764;)V"},
      at = {@At("HEAD")}
   )
   private void notifyControllerGameReady(CallbackInfo ci) {
      SplitscreenBootstrapper.getControllerBridge().ifPresent((bridge) -> {
         bridge.signalImReady(true, 1.0F);
      });
   }

   @Inject(
      method = {"method_18502(Lnet/minecraft/class_4071;)V"},
      at = {@At("RETURN")}
   )
   private void updateSplitscreenWhenOverlayChanges(CallbackInfo ci) {
      SplitscreenBootstrapper.getController().ifPresent(SplitscreenController::updateSplitscreenMode);
   }
}
