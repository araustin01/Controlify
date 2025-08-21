package dev.isxander.splitscreen.client.mixins.engine.reparent;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.isxander.splitscreen.client.SplitscreenBootstrapper;
import dev.isxander.splitscreen.client.engine.impl.reparenting.ReparentingHostSplitscreenEngine;
import dev.isxander.splitscreen.client.engine.impl.reparenting.ReparentingRemoteSplitscreenEngine;
import dev.isxander.splitscreen.client.engine.impl.reparenting.parent.ParentWindow;
import java.util.Optional;
import net.minecraft.class_310;
import net.minecraft.class_3682;
import net.minecraft.class_543;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_310.class})
public abstract class MinecraftMixin {
   @Shadow
   @Final
   private class_3682 field_1686;

   @Shadow
   protected abstract String method_24287();

   @ModifyArg(
      method = {"<init>(Lnet/minecraft/class_542;)V"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_3682;method_16038(Lnet/minecraft/class_543;Ljava/lang/String;Ljava/lang/String;)Lnet/minecraft/class_1041;"
)
   )
   private class_543 captureDisplayData(class_543 displayData, @Share("screenSize") LocalRef<class_543> screenSize) {
      screenSize.set(displayData);
      return displayData;
   }

   @WrapWithCondition(
      method = {"<init>(Lnet/minecraft/class_542;)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_310;method_15993()V"
)}
   )
   private boolean markWindowReady(class_310 instance, @Share("screenSize") LocalRef<class_543> screenSize) {
      SplitscreenBootstrapper.getPawn().flatMap(ReparentingRemoteSplitscreenEngine::tryGet).ifPresent(ReparentingRemoteSplitscreenEngine::onWindowInit);
      return (Boolean)SplitscreenBootstrapper.getController().flatMap(ReparentingHostSplitscreenEngine::tryGet).map((engine) -> {
         engine.initWindow((class_543)screenSize.get(), ((VirtualScreenAccessor)this.field_1686).getScreenManager(), this.method_24287());
         return false;
      }).orElse(true);
   }

   @Inject(
      method = {"close()V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_1041;close()V"
)}
   )
   private void closeParentWindow(CallbackInfo ci) {
      SplitscreenBootstrapper.getController().flatMap(ReparentingHostSplitscreenEngine::tryGet).flatMap((engine) -> {
         return Optional.ofNullable(engine.getParentWindow());
      }).ifPresent(ParentWindow::close);
   }
}
