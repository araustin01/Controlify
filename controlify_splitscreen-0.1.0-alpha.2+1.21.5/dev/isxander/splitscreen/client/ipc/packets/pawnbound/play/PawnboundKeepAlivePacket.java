package dev.isxander.splitscreen.client.ipc.packets.pawnbound.play;

import dev.isxander.splitscreen.client.remote.ipc.PawnPlayPacketListener;
import net.minecraft.class_2540;
import net.minecraft.class_2596;
import net.minecraft.class_9139;
import net.minecraft.class_9145;

public record PawnboundKeepAlivePacket() implements PawnboundPlayPacket {
   public static final PawnboundKeepAlivePacket INSTANCE = new PawnboundKeepAlivePacket();
   public static final class_9139<class_2540, PawnboundKeepAlivePacket> CODEC;
   public static final class_9145<PawnboundKeepAlivePacket> TYPE;

   public void handle(PawnPlayPacketListener listener) {
      listener.handleKeepAlive(this);
   }

   public class_9145<? extends class_2596<PawnPlayPacketListener>> method_65080() {
      return TYPE;
   }

   static {
      CODEC = class_9139.method_56431(INSTANCE);
      TYPE = PawnboundPlayPacket.createType("keep_alive");
   }
}
