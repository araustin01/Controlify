package dev.isxander.splitscreen.server;

import dev.isxander.splitscreen.client.SplitscreenBootstrapper;
import dev.isxander.splitscreen.client.host.SplitscreenController;
import dev.isxander.splitscreen.server.login.SplitscreenLoginFlowClient;
import dev.isxander.splitscreen.server.play.sound.ClientboundBundledSoundEntityPacket;
import dev.isxander.splitscreen.server.play.sound.ClientboundBundledSoundPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.class_2765;
import net.minecraft.class_2767;

public class SplitscreenSSClient {
   public static void init() {
      SplitscreenSSServer.init();
      SplitscreenLoginFlowClient.init();
      initSound();
   }

   private static void initSound() {
      ClientPlayNetworking.registerGlobalReceiver(ClientboundBundledSoundPacket.TYPE, (payload, ctx) -> {
         SplitscreenController controller = (SplitscreenController)SplitscreenBootstrapper.getController().orElseThrow(() -> {
            return new IllegalStateException("Server sent bundled sound packet to non-controller.");
         });
         BundledPacketInfo bundleInfo = payload.bundleInfo();
         class_2767 soundPacket = payload.packet();
         if (bundleInfo.includeController()) {
            soundPacket.method_11895(ctx.client().method_1562());
         }

      });
      ClientPlayNetworking.registerGlobalReceiver(ClientboundBundledSoundEntityPacket.TYPE, (payload, ctx) -> {
         SplitscreenController controller = (SplitscreenController)SplitscreenBootstrapper.getController().orElseThrow(() -> {
            return new IllegalStateException("Server sent bundled sound packet to non-controller.");
         });
         BundledPacketInfo bundleInfo = payload.bundleInfo();
         class_2765 soundPacket = payload.packet();
         if (bundleInfo.includeController()) {
            soundPacket.method_11884(ctx.client().method_1562());
         }

      });
   }
}
