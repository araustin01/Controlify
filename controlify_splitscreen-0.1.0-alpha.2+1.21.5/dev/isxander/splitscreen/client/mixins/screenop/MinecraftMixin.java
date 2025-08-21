package dev.isxander.splitscreen.client.mixins.screenop;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Definitions;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.isxander.splitscreen.client.SplitscreenBootstrapper;
import dev.isxander.splitscreen.client.features.screenop.ScreenSplitscreenMode;
import dev.isxander.splitscreen.client.features.screenop.ScreenSplitscreenModeRegistry;
import dev.isxander.splitscreen.client.remote.gui.ImHiddenScreen;
import net.minecraft.class_310;
import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_310.class})
public class MinecraftMixin {
   @ModifyExpressionValue(
      method = {"method_1507(Lnet/minecraft/class_437;)V"},
      at = {@At("MIXINEXTRAS:EXPRESSION")}
   )
   @Definitions({@Definition(
   id = "screen",
   field = {"Lnet/minecraft/class_310;field_1755:Lnet/minecraft/class_437;"}
), @Definition(
   id = "newScreen",
   local = {@Local(
   type = class_437.class,
   argsOnly = true
)}
)})
   @Expression({"this.screen = @(newScreen)"})
   private class_437 overrideScreenIfPawnFullscreen(class_437 newScreen, @Local(argsOnly = true) LocalRef<class_437> newScreenRef, @Share("splitscreenMode") LocalRef<ScreenSplitscreenMode> splitscreenModeRef) {
      ScreenSplitscreenMode splitscreenMode = ScreenSplitscreenModeRegistry.getMode(newScreen);
      splitscreenModeRef.set(splitscreenMode);
      if (splitscreenMode == ScreenSplitscreenMode.FULLSCREEN && SplitscreenBootstrapper.getPawn().isPresent()) {
         System.out.println("Attempted to set " + newScreen.getClass().getSimpleName() + " as the screen on a pawn client.");
         ImHiddenScreen hiddenScreen = new ImHiddenScreen();
         newScreenRef.set(hiddenScreen);
         return hiddenScreen;
      } else {
         return newScreen;
      }
   }

   @Inject(
      method = {"method_1507(Lnet/minecraft/class_437;)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_437;method_25423(Lnet/minecraft/class_310;II)V",
   shift = Shift.AFTER
), @At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_1144;method_4880()V"
)}
   )
   private void updateSplitscreenMode(CallbackInfo ci, @Share("splitscreenMode") LocalRef<ScreenSplitscreenMode> splitscreenModeRef) {
      ScreenSplitscreenMode splitscreenMode = (ScreenSplitscreenMode)splitscreenModeRef.get();
      SplitscreenBootstrapper.getController().ifPresent((controller) -> {
         controller.setSplitscreenMode(splitscreenMode);
      });
   }
}
