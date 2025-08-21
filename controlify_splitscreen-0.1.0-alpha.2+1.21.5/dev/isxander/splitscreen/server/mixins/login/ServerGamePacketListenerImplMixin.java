package dev.isxander.splitscreen.server.mixins.login;

import com.mojang.authlib.GameProfile;
import dev.isxander.splitscreen.server.login.SplitscreenLoginFlowServer;
import net.minecraft.class_3244;
import net.minecraft.class_9812;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_3244.class})
public abstract class ServerGamePacketListenerImplMixin {
   @Shadow
   protected abstract GameProfile method_52403();

   @Inject(
      method = {"method_10839(Lnet/minecraft/class_9812;)V"},
      at = {@At("HEAD")}
   )
   private void onDisconnect(class_9812 details, CallbackInfo ci) {
      SplitscreenLoginFlowServer.onClientDisconnect(this.method_52403(), details);
   }
}
