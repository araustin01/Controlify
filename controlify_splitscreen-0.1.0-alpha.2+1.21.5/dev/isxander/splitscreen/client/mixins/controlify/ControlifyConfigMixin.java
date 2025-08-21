package dev.isxander.splitscreen.client.mixins.controlify;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.isxander.controlify.config.ControlifyConfig;
import dev.isxander.splitscreen.client.SplitscreenBootstrapper;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ControlifyConfig.class})
public class ControlifyConfigMixin {
   @WrapMethod(
      method = {"save()V"}
   )
   private void preventSaveIfPawn(Operation<Void> original) {
      if (SplitscreenBootstrapper.getPawn().isEmpty()) {
         original.call(new Object[0]);
      }

   }
}
