package dev.isxander.splitscreen.server.mixins.status;

import dev.isxander.splitscreen.server.status.ServerStatusSplitscreenExt;
import net.minecraft.class_642;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({class_642.class})
public class ServerDataMixin implements ServerStatusSplitscreenExt.Duck {
   @Unique
   private ServerStatusSplitscreenExt splitscreen$ext;

   public ServerStatusSplitscreenExt splitscreen$getExt() {
      return this.splitscreen$ext;
   }

   public void splitscreen$setExt(ServerStatusSplitscreenExt ext) {
      this.splitscreen$ext = ext;
   }
}
