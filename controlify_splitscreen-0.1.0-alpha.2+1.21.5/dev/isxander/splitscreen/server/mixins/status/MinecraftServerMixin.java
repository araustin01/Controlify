package dev.isxander.splitscreen.server.mixins.status;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.isxander.splitscreen.server.login.SplitscreenLoginFlowServer;
import dev.isxander.splitscreen.server.status.ServerStatusSplitscreenExt;
import java.util.Objects;
import net.minecraft.class_2926;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({MinecraftServer.class})
public class MinecraftServerMixin {
   @WrapMethod(
      method = {"method_49385()Lnet/minecraft/class_2926;"}
   )
   private class_2926 addSplitscreenInfoToServerStatus(Operation<class_2926> operation) {
      ServerStatusSplitscreenExt splitscreenExt = SplitscreenLoginFlowServer.buildSplitscreenStatus();
      Objects.requireNonNull(operation);
      return (class_2926)ServerStatusSplitscreenExt.construct(() -> {
         return (class_2926)operation.call(new Object[0]);
      }, splitscreenExt);
   }
}
