package dev.isxander.splitscreen.client.mixins.framethrottle;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.isxander.splitscreen.client.SplitscreenBootstrapper;
import dev.isxander.splitscreen.client.engine.impl.reparenting.ReparentingRemoteSplitscreenEngine;
import dev.isxander.splitscreen.client.remote.RemotePawnMain;
import java.util.Optional;
import net.minecraft.class_9919;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({class_9919.class})
public class FramerateLimitTrackerMixin {
   @WrapMethod(
      method = {"method_61937()I"}
   )
   private int throttleFramerateIfHidden(Operation<Integer> original) {
      return this.shouldSplitscreenThrottle() ? 1 : (Integer)original.call(new Object[0]);
   }

   @WrapMethod(
      method = {"method_66515()Z"}
   )
   private boolean sayHeavilyThrottledIfHidden(Operation<Boolean> original) {
      return this.shouldSplitscreenThrottle() ? true : (Boolean)original.call(new Object[0]);
   }

   @Unique
   private boolean shouldSplitscreenThrottle() {
      return (Boolean)SplitscreenBootstrapper.getPawn().map(RemotePawnMain::getSplitscreenEngine).flatMap((engine) -> {
         Optional var10000;
         if (engine instanceof ReparentingRemoteSplitscreenEngine) {
            ReparentingRemoteSplitscreenEngine reparentingEngine = (ReparentingRemoteSplitscreenEngine)engine;
            var10000 = Optional.of(reparentingEngine);
         } else {
            var10000 = Optional.empty();
         }

         return var10000;
      }).map(ReparentingRemoteSplitscreenEngine::shouldThrottleFps).orElse(false);
   }
}
