package dev.isxander.splitscreen.client.mixins.screenop;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.splitscreen.client.SplitscreenBootstrapper;
import dev.isxander.splitscreen.client.features.screenop.ScreenSplitscreenMode;
import dev.isxander.splitscreen.client.features.screenop.ScreenSplitscreenModeRegistry;
import net.minecraft.class_310;
import net.minecraft.class_437;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_437.class})
public abstract class ScreenMixin {
   @Shadow
   @Nullable
   protected class_310 field_22787;

   @ModifyExpressionValue(
      method = {"method_25420(Lnet/minecraft/class_332;IIF)V"},
      at = {@At("MIXINEXTRAS:EXPRESSION")}
   )
   @Definition(
      id = "level",
      field = {"Lnet/minecraft/class_310;field_1687:Lnet/minecraft/class_638;"}
   )
   @Expression({"?.level == null"})
   private boolean shouldRenderPanorama(boolean hasNoLevel) {
      boolean isSplitscreen = SplitscreenBootstrapper.isSplitscreen();
      boolean inFullscreenMode = ScreenSplitscreenModeRegistry.getMode((class_437)this) == ScreenSplitscreenMode.FULLSCREEN;
      boolean isIntegratedServer = this.field_22787.method_1576() != null;
      return hasNoLevel || isSplitscreen && inFullscreenMode && !isIntegratedServer;
   }
}
