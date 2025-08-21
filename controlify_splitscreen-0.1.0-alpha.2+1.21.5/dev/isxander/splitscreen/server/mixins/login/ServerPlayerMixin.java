package dev.isxander.splitscreen.server.mixins.login;

import dev.isxander.splitscreen.server.SplitscreenPlayerInfo;
import net.minecraft.class_3222;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({class_3222.class})
public class ServerPlayerMixin implements SplitscreenPlayerInfo.SplitscreenPlayerInfoHolder {
   @Unique
   @Nullable
   private SplitscreenPlayerInfo splitscreen$playerInfo;

   @Nullable
   public SplitscreenPlayerInfo splitscreen$getPlayerInfo() {
      return this.splitscreen$playerInfo;
   }

   public void splitscreen$setPlayerInfo(SplitscreenPlayerInfo playerInfo) {
      this.splitscreen$playerInfo = playerInfo;
   }
}
