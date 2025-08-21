package dev.isxander.splitscreen.client.ipc.packets.controllerbound.play;

import dev.isxander.splitscreen.client.host.ipc.ControllerPlayPacketListener;
import net.minecraft.class_8710;
import net.minecraft.class_9145;

public record ControllerboundEngineCustomPayloadPacket(class_8710 payload) implements ControllerboundPlayPacket {
   public static final class_9145<ControllerboundEngineCustomPayloadPacket> TYPE = ControllerboundPlayPacket.createType("engine_custom_payload");

   public ControllerboundEngineCustomPayloadPacket(class_8710 payload) {
      this.payload = payload;
   }

   public void handle(ControllerPlayPacketListener handler) {
      handler.handleEngineCustomPayload(this);
   }

   public class_9145<ControllerboundEngineCustomPayloadPacket> method_65080() {
      return TYPE;
   }

   public class_8710 payload() {
      return this.payload;
   }
}
