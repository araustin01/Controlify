package dev.isxander.splitscreen.client.ipc.packets.controllerbound.handshake;

import dev.isxander.splitscreen.client.host.ipc.ControllerHandshakePacketListener;
import dev.isxander.splitscreen.util.CSUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.class_2596;
import net.minecraft.class_2598;
import net.minecraft.class_9135;
import net.minecraft.class_9139;
import net.minecraft.class_9145;

public record ControllerboundHandshakePacket(int protocolVersion) implements class_2596<ControllerHandshakePacketListener> {
   public static final class_9139<ByteBuf, ControllerboundHandshakePacket> CODEC;
   public static final class_9145<ControllerboundHandshakePacket> TYPE;

   public ControllerboundHandshakePacket(int protocolVersion) {
      this.protocolVersion = protocolVersion;
   }

   public void handle(ControllerHandshakePacketListener handler) {
      handler.handleHandshake(this);
   }

   public class_9145<? extends class_2596<ControllerHandshakePacketListener>> method_65080() {
      return TYPE;
   }

   public boolean method_55943() {
      return true;
   }

   public int protocolVersion() {
      return this.protocolVersion;
   }

   static {
      CODEC = class_9135.field_49675.method_56432(ControllerboundHandshakePacket::new, ControllerboundHandshakePacket::protocolVersion);
      TYPE = new class_9145(class_2598.field_11941, CSUtil.rl("handshake"));
   }
}
