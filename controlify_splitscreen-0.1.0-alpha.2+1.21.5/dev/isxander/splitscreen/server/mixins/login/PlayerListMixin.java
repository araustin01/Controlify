package dev.isxander.splitscreen.server.mixins.login;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import dev.isxander.splitscreen.server.SplitscreenPlayerInfo;
import dev.isxander.splitscreen.server.login.SplitscreenLoginFlowServer;
import net.minecraft.class_3222;
import net.minecraft.class_3324;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_3324.class})
public class PlayerListMixin {
   @Shadow
   @Final
   private MinecraftServer field_14360;

   @ModifyReturnValue(
      method = {"method_14613(Lcom/mojang/authlib/GameProfile;Lnet/minecraft/class_8791;)Lnet/minecraft/class_3222;"},
      at = {@At("RETURN")}
   )
   private class_3222 attachSplitscreenInfoAtLogin(class_3222 player, @Local(argsOnly = true) GameProfile gameProfile) {
      SplitscreenLoginFlowServer.ControllerState state = SplitscreenLoginFlowServer.getStateFromControllerOrSubplayer(gameProfile.getId());
      if (state != null) {
         SplitscreenPlayerInfo.SplitscreenPlayerInfoHolder holder = (SplitscreenPlayerInfo.SplitscreenPlayerInfoHolder)player;
         if (state.hostProfile().equals(gameProfile)) {
            holder.splitscreen$setPlayerInfo(new SplitscreenPlayerInfo.Controller(state.subPlayerProfiles(), state.sharedConfig(), this.field_14360, player));
         } else {
            int subPlayerIndex = state.getSubPlayerIndex(player.method_7334());
            if (subPlayerIndex == -1) {
               throw new IllegalStateException("Player " + player.method_7334().getName() + " has no subplayer index");
            }

            holder.splitscreen$setPlayerInfo(new SplitscreenPlayerInfo.SubPlayer(state.hostProfile(), state.sharedConfig(), subPlayerIndex, this.field_14360, player));
         }
      }

      return player;
   }

   @ModifyExpressionValue(
      method = {"method_14556(Lnet/minecraft/class_3222;ZLnet/minecraft/class_1297$class_5529;)Lnet/minecraft/class_3222;"},
      at = {@At(
   value = "NEW",
   target = "Lnet/minecraft/class_3222;"
)}
   )
   private class_3222 attachSplitscreenInfoAtRespawn(class_3222 player, @Local(argsOnly = true) class_3222 oldPlayer) {
      ((SplitscreenPlayerInfo.SplitscreenPlayerInfoHolder)player).splitscreen$setPlayerInfo((SplitscreenPlayerInfo)SplitscreenPlayerInfo.get(oldPlayer).orElse((Object)null));
      return player;
   }
}
