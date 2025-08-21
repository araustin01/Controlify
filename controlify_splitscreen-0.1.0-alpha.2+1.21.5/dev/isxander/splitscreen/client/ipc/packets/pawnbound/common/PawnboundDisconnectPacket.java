package dev.isxander.splitscreen.client.ipc.packets.pawnbound.common;

import dev.isxander.splitscreen.client.remote.ipc.PawnboundCommonPacketListener;
import io.netty.buffer.ByteBuf;
import net.minecraft.class_2561;
import net.minecraft.class_2596;
import net.minecraft.class_2598;
import net.minecraft.class_2960;
import net.minecraft.class_8824;
import net.minecraft.class_9139;
import net.minecraft.class_9145;

public record PawnboundDisconnectPacket(class_2561 reason) implements class_2596<PawnboundCommonPacketListener> {
   public static final class_9139<ByteBuf, PawnboundDisconnectPacket> CODEC;
   public static final class_9145<PawnboundDisconnectPacket> TYPE;

   public PawnboundDisconnectPacket(class_2561 reason) {
      this.reason = reason;
   }

   public void handle(PawnboundCommonPacketListener handler) {
      handler.handleDisconnect(this);
   }

   public class_9145<? extends class_2596<PawnboundCommonPacketListener>> method_65080() {
      return TYPE;
   }

   public class_2561 reason() {
      return this.reason;
   }

   static {
      CODEC = class_8824.field_49668.method_56432(PawnboundDisconnectPacket::new, PawnboundDisconnectPacket::reason);
      TYPE = new class_9145(class_2598.field_11942, class_2960.method_60656("disconnect"));
   }
}
