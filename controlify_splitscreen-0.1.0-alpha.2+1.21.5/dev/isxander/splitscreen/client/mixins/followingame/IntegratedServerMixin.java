package dev.isxander.splitscreen.client.mixins.followingame;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Definitions;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import dev.isxander.splitscreen.client.SplitscreenBootstrapper;
import dev.isxander.splitscreen.client.host.util.LANUtil;
import java.net.InetAddress;
import net.minecraft.class_1132;
import net.minecraft.class_310;
import net.minecraft.class_634;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin({class_1132.class})
public class IntegratedServerMixin {
   @Shadow
   @Final
   private class_310 field_5518;

   @ModifyArg(
      method = {"method_3763(Lnet/minecraft/class_1934;ZI)Z"},
      at = @At("MIXINEXTRAS:EXPRESSION")
   )
   @Definitions({@Definition(
   id = "startTcpServerListener",
   method = {"Lnet/minecraft/class_3242;method_14354(Ljava/net/InetAddress;I)V"}
), @Definition(
   id = "port",
   local = {@Local(
   type = int.class,
   argsOnly = true
)}
)})
   @Expression({"?.startTcpServerListener(null, port)"})
   private InetAddress modifyLANBindAddress(InetAddress original) {
      return !SplitscreenBootstrapper.isSplitscreen() ? original : LANUtil.getLANServerBindAddress();
   }

   @WrapOperation(
      method = {"method_3763(Lnet/minecraft/class_1934;ZI)Z"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_634;method_62151()V"
)}
   )
   private void redirectPrepareKeyPair(class_634 instance, Operation<Void> original) {
      if (instance == null) {
         this.field_5518.method_43590().method_46522();
      } else {
         original.call(new Object[]{instance});
      }

   }

   @WrapOperation(
      method = {"method_3763(Lnet/minecraft/class_1934;ZI)Z"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_746;method_7334()Lcom/mojang/authlib/GameProfile;"
)}
   )
   private GameProfile preventPlayerNPE0(class_746 instance, Operation<GameProfile> original) {
      return instance == null ? null : (GameProfile)original.call(new Object[]{instance});
   }

   @WrapOperation(
      method = {"method_3763(Lnet/minecraft/class_1934;ZI)Z"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_1132;method_3835(Lcom/mojang/authlib/GameProfile;)I"
)}
   )
   private int preventPlayerNPE1(class_1132 instance, GameProfile gameProfile, Operation<Integer> original) {
      return gameProfile == null ? 0 : (Integer)original.call(new Object[]{instance, gameProfile});
   }

   @WrapOperation(
      method = {"method_3763(Lnet/minecraft/class_1934;ZI)Z"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_746;method_3147(I)V"
)}
   )
   private void preventPlayerNPE2(class_746 instance, int permissionLevel, Operation<Void> original) {
      if (instance != null) {
         original.call(new Object[]{instance, permissionLevel});
      }
   }
}
