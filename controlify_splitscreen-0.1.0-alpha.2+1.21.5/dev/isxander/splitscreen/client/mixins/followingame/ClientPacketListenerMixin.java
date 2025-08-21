package dev.isxander.splitscreen.client.mixins.followingame;

import dev.isxander.splitscreen.client.SplitscreenBootstrapper;
import dev.isxander.splitscreen.client.host.util.LANUtil;
import net.minecraft.class_1132;
import net.minecraft.class_2535;
import net.minecraft.class_310;
import net.minecraft.class_634;
import net.minecraft.class_639;
import net.minecraft.class_8673;
import net.minecraft.class_8675;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_634.class})
public abstract class ClientPacketListenerMixin extends class_8673 {
   @Shadow
   @Final
   private static Logger field_3695;

   protected ClientPacketListenerMixin(class_310 minecraft, class_2535 connection, class_8675 commonListenerCookie) {
      super(minecraft, connection, commonListenerCookie);
   }

   @Inject(
      method = {"method_11120(Lnet/minecraft/class_2678;)V"},
      at = {@At("RETURN")}
   )
   private void forceSplitscreenToJoin(CallbackInfo ci) {
      SplitscreenBootstrapper.getController().ifPresent((controller) -> {
         byte[] nonce = controller.getLocalPawn().getLastLoginNonce();
         if (this.field_45588.method_1496()) {
            if (nonce == null) {
               field_3695.error("Nonce has not been set, join attempt will probably fail.");
            }

            class_1132 server = this.field_45588.method_1576();
            class_639 address = LANUtil.getOrPublishLANServer(server);
            controller.forEachPawn((pawn) -> {
               pawn.joinServer(address.method_2952(), address.method_2954(), nonce);
            });
         } else if (nonce == null) {
         }

      });
   }
}
