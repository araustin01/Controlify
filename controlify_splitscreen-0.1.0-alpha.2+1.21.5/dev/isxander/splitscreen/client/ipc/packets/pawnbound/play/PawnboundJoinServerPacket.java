package dev.isxander.splitscreen.client.ipc.packets.pawnbound.play;

import dev.isxander.splitscreen.client.remote.ipc.PawnPlayPacketListener;
import java.util.Optional;
import net.minecraft.class_2540;
import net.minecraft.class_2596;
import net.minecraft.class_9135;
import net.minecraft.class_9139;
import net.minecraft.class_9145;

public record PawnboundJoinServerPacket(String host, int port, Optional<byte[]> nonce) implements PawnboundPlayPacket {
   public static final class_9139<class_2540, PawnboundJoinServerPacket> CODEC;
   public static final class_9145<PawnboundJoinServerPacket> TYPE;

   public PawnboundJoinServerPacket(String host, int port, Optional<byte[]> nonce) {
      this.host = host;
      this.port = port;
      this.nonce = nonce;
   }

   public void handle(PawnPlayPacketListener handler) {
      handler.handleJoinServer(this);
   }

   public class_9145<? extends class_2596<PawnPlayPacketListener>> method_65080() {
      return TYPE;
   }

   public String host() {
      return this.host;
   }

   public int port() {
      return this.port;
   }

   public Optional<byte[]> nonce() {
      return this.nonce;
   }

   static {
      CODEC = class_9139.method_56436(class_9135.field_48554, PawnboundJoinServerPacket::host, class_9135.field_49675, PawnboundJoinServerPacket::port, class_9135.method_56382(class_9135.method_56895(16)), PawnboundJoinServerPacket::nonce, PawnboundJoinServerPacket::new);
      TYPE = PawnboundPlayPacket.createType("join_server");
   }
}
