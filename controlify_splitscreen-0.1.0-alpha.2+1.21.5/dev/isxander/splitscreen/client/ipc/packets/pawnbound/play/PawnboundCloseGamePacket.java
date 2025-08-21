package dev.isxander.splitscreen.client.ipc.packets.pawnbound.play;

import dev.isxander.splitscreen.client.remote.ipc.PawnPlayPacketListener;
import net.minecraft.class_2540;
import net.minecraft.class_9139;
import net.minecraft.class_9145;

public record PawnboundCloseGamePacket() implements PawnboundPlayPacket {
   public static final PawnboundCloseGamePacket INSTANCE = new PawnboundCloseGamePacket();
   public static final class_9139<class_2540, PawnboundCloseGamePacket> CODEC;
   public static final class_9145<PawnboundCloseGamePacket> TYPE;

   public void handle(PawnPlayPacketListener handler) {
      handler.handleCloseGame(this);
   }

   public class_9145<PawnboundCloseGamePacket> method_65080() {
      return TYPE;
   }

   static {
      CODEC = class_9139.method_56431(INSTANCE);
      TYPE = PawnboundPlayPacket.createType("close_game");
   }
}
