package dev.isxander.splitscreen.client.engine;

import dev.isxander.splitscreen.client.ipc.packets.controllerbound.play.ControllerboundEngineCustomPayloadPacket;
import dev.isxander.splitscreen.client.ipc.packets.pawnbound.play.PawnboundEngineCustomPayloadPacket;
import net.minecraft.class_2535;
import net.minecraft.class_8710;

public interface SplitscreenEnginePayloadSender {
   static SplitscreenEnginePayloadSender controllerbound(class_2535 connection) {
      return (payload) -> {
         connection.method_10743(new ControllerboundEngineCustomPayloadPacket(payload));
      };
   }

   static SplitscreenEnginePayloadSender pawnbound(class_2535 connection) {
      return (payload) -> {
         connection.method_10743(new PawnboundEngineCustomPayloadPacket(payload));
      };
   }

   void sendPayload(class_8710 var1);
}
