package dev.isxander.splitscreen.client.ipc.packets.pawnbound.play;

import dev.isxander.splitscreen.client.remote.ipc.PawnPlayPacketListener;
import net.minecraft.class_8710;
import net.minecraft.class_9145;

public record PawnboundEngineCustomPayloadPacket(class_8710 payload) implements PawnboundPlayPacket {
   public static final class_9145<PawnboundEngineCustomPayloadPacket> TYPE = PawnboundPlayPacket.createType("engine_custom_payload");

   public PawnboundEngineCustomPayloadPacket(class_8710 payload) {
      this.payload = payload;
   }

   public void handle(PawnPlayPacketListener handler) {
      handler.handleEngineCustomPayload(this);
   }

   public class_9145<PawnboundEngineCustomPayloadPacket> method_65080() {
      return TYPE;
   }

   public class_8710 payload() {
      return this.payload;
   }
}
