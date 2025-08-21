package dev.isxander.splitscreen.client.ipc.packets.pawnbound.play;

import dev.isxander.splitscreen.client.remote.ipc.PawnPlayPacketListener;
import net.minecraft.class_2540;
import net.minecraft.class_9139;
import net.minecraft.class_9145;

public record PawnboundServerDisconnectPacket() implements PawnboundPlayPacket {
   public static final PawnboundServerDisconnectPacket UNIT = new PawnboundServerDisconnectPacket();
   public static final class_9139<class_2540, PawnboundServerDisconnectPacket> CODEC;
   public static final class_9145<PawnboundServerDisconnectPacket> TYPE;

   public void handle(PawnPlayPacketListener handler) {
      handler.handleServerDisconnect(this);
   }

   public class_9145<PawnboundServerDisconnectPacket> method_65080() {
      return TYPE;
   }

   static {
      CODEC = class_9139.method_56431(UNIT);
      TYPE = PawnboundPlayPacket.createType("server_disconnected");
   }
}
