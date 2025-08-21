package dev.isxander.splitscreen.client.ipc.packets.pawnbound.play;

import dev.isxander.controlify.controller.ControllerUID;
import dev.isxander.splitscreen.client.ipc.utils.ExtraStreamCodecs;
import dev.isxander.splitscreen.client.remote.ipc.PawnPlayPacketListener;
import io.netty.buffer.ByteBuf;
import net.minecraft.class_9139;
import net.minecraft.class_9145;

public record PawnboundUseControllerPacket(ControllerUID controllerUID) implements PawnboundPlayPacket {
   public static final class_9139<ByteBuf, PawnboundUseControllerPacket> CODEC;
   public static final class_9145<PawnboundUseControllerPacket> TYPE;

   public PawnboundUseControllerPacket(ControllerUID controllerUID) {
      this.controllerUID = controllerUID;
   }

   public void handle(PawnPlayPacketListener handler) {
      handler.handleUseController(this);
   }

   public class_9145<PawnboundUseControllerPacket> method_65080() {
      return TYPE;
   }

   public ControllerUID controllerUID() {
      return this.controllerUID;
   }

   static {
      CODEC = ExtraStreamCodecs.CONTROLLER_UID.method_56432(PawnboundUseControllerPacket::new, PawnboundUseControllerPacket::controllerUID);
      TYPE = PawnboundPlayPacket.createType("use_controller");
   }
}
