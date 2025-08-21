package dev.isxander.splitscreen.client.host.ipc;

import dev.isxander.splitscreen.client.engine.SplitscreenEngine;
import dev.isxander.splitscreen.client.host.SplitscreenController;
import dev.isxander.splitscreen.client.ipc.packets.PlayProtocols;
import dev.isxander.splitscreen.client.ipc.packets.controllerbound.handshake.ControllerboundHandshakePacket;
import dev.isxander.splitscreen.client.ipc.packets.pawnbound.common.PawnboundDisconnectPacket;
import net.minecraft.class_2535;
import net.minecraft.class_2539;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_5250;
import net.minecraft.class_6857;
import net.minecraft.class_9812;

public class ControllerHandshakePacketListener implements ControllerboundCommonPacketListener, class_6857 {
   private final SplitscreenController controller;
   private final class_2535 connection;
   private final class_310 minecraft;

   public ControllerHandshakePacketListener(SplitscreenController controller, class_2535 connection, class_310 minecraft) {
      this.controller = controller;
      this.connection = connection;
      this.minecraft = minecraft;
   }

   public void handleHandshake(ControllerboundHandshakePacket packet) {
      SplitscreenEngine splitscreenEngine = this.controller.getSplitscreenEngine();
      this.connection.method_56329(PlayProtocols.pawnbound(splitscreenEngine.getPawnboundCustomPayloadCodec()));
      if (packet.protocolVersion() != 1) {
         class_5250 reason = class_2561.method_43470("Unsupported protocol version: " + packet.protocolVersion());
         this.connection.method_10743(new PawnboundDisconnectPacket(reason));
         this.connection.method_10747(reason);
      } else {
         this.connection.method_56330(PlayProtocols.controllerbound(splitscreenEngine.getControllerboundCustomPayloadCodec()), new ControllerPlayPacketListener(this.controller, this.connection, this.minecraft));
      }
   }

   public class_2539 method_52280() {
      return class_2539.field_20590;
   }

   public void method_10839(class_9812 details) {
   }

   public boolean method_48106() {
      return this.connection.method_10758();
   }
}
