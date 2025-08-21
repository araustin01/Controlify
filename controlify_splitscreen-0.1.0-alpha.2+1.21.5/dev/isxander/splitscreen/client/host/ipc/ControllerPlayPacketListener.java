package dev.isxander.splitscreen.client.host.ipc;

import com.mojang.logging.LogUtils;
import dev.isxander.splitscreen.client.host.RemoteSplitscreenPawn;
import dev.isxander.splitscreen.client.host.SplitscreenController;
import dev.isxander.splitscreen.client.ipc.packets.controllerbound.play.ControllerboundEngineCustomPayloadPacket;
import dev.isxander.splitscreen.client.ipc.packets.controllerbound.play.ControllerboundGiveMeFocusIfForegroundPacket;
import dev.isxander.splitscreen.client.ipc.packets.controllerbound.play.ControllerboundHelloPacket;
import dev.isxander.splitscreen.client.ipc.packets.controllerbound.play.ControllerboundKeepAlivePacket;
import dev.isxander.splitscreen.client.ipc.packets.controllerbound.play.ControllerboundRequestPlayMusicPacket;
import dev.isxander.splitscreen.client.ipc.packets.controllerbound.play.ControllerboundServerDisconnectedPacket;
import dev.isxander.splitscreen.client.ipc.packets.controllerbound.play.ControllerboundSignalReadyPacket;
import dev.isxander.splitscreen.client.ipc.packets.pawnbound.play.PawnboundKeepAlivePacket;
import net.minecraft.class_2535;
import net.minecraft.class_2539;
import net.minecraft.class_2600;
import net.minecraft.class_310;
import net.minecraft.class_5195;
import net.minecraft.class_6857;
import net.minecraft.class_9812;
import org.slf4j.Logger;

public class ControllerPlayPacketListener implements ControllerboundCommonPacketListener, class_6857 {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final SplitscreenController controller;
   private RemoteSplitscreenPawn pawnInstance;
   private final class_2535 connection;
   private final class_310 minecraft;

   public ControllerPlayPacketListener(SplitscreenController controller, class_2535 connection, class_310 minecraft) {
      this.controller = controller;
      this.connection = connection;
      this.minecraft = minecraft;
   }

   public void handleHello(ControllerboundHelloPacket packet) {
      class_2600.method_11074(packet, this, this.minecraft);
      this.pawnInstance = new RemoteSplitscreenPawn(this.connection, this.controller.getNextPawnIndex(), packet.controller());
      this.controller.addPawn(this.pawnInstance);
      this.connection.method_10743(PawnboundKeepAlivePacket.INSTANCE);
   }

   public void handleGiveChildFocusIfForeground(ControllerboundGiveMeFocusIfForegroundPacket packet) {
      this.minecraft.execute(() -> {
      });
   }

   public void handleReadySignal(ControllerboundSignalReadyPacket packet) {
      class_2600.method_11074(packet, this, this.minecraft);
      this.controller.getControllerBridge().signalRemoteClientReady(packet.finished(), packet.progress(), this.pawnInstance, this.pawnInstance.getAssociatedController());
   }

   public void handleKeepAlive(ControllerboundKeepAlivePacket packet) {
      this.connection.method_10743(PawnboundKeepAlivePacket.INSTANCE);
   }

   public void handleEngineCustomPayload(ControllerboundEngineCustomPayloadPacket packet) {
      class_2600.method_11074(packet, this, this.minecraft);
      this.controller.getSplitscreenEngine().handleInboundPayload(this.pawnInstance.getAssociatedController(), this.connection, packet.payload());
   }

   public void handleServerDisconnected(ControllerboundServerDisconnectedPacket packet) {
      class_2600.method_11074(packet, this, this.minecraft);
      this.controller.getControllerBridge().serverDisconnectedRemote(packet.disconnectReason(), this.pawnInstance);
   }

   public void handleRequestMusic(ControllerboundRequestPlayMusicPacket packet) {
      class_2600.method_11074(packet, this, this.minecraft);
      this.controller.getControllerBridge().requestPlayMusicRemote((class_5195)packet.music().orElse((Object)null), packet.volume(), this.pawnInstance);
   }

   public void method_10839(class_9812 details) {
      this.minecraft.execute(() -> {
         this.controller.removePawn(this.pawnInstance);
      });
   }

   public class_2539 method_52280() {
      return class_2539.field_20591;
   }

   public boolean method_48106() {
      return this.connection.method_10758();
   }
}
