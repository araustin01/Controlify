package dev.isxander.splitscreen.client.remote.ipc;

import com.mojang.logging.LogUtils;
import dev.isxander.splitscreen.client.LocalSplitscreenPawn;
import dev.isxander.splitscreen.client.ipc.packets.controllerbound.play.ControllerboundKeepAlivePacket;
import dev.isxander.splitscreen.client.ipc.packets.pawnbound.common.PawnboundDisconnectPacket;
import dev.isxander.splitscreen.client.ipc.packets.pawnbound.play.PawnboundCloseGamePacket;
import dev.isxander.splitscreen.client.ipc.packets.pawnbound.play.PawnboundEngineCustomPayloadPacket;
import dev.isxander.splitscreen.client.ipc.packets.pawnbound.play.PawnboundJoinServerPacket;
import dev.isxander.splitscreen.client.ipc.packets.pawnbound.play.PawnboundKeepAlivePacket;
import dev.isxander.splitscreen.client.ipc.packets.pawnbound.play.PawnboundLoadConfigPacket;
import dev.isxander.splitscreen.client.ipc.packets.pawnbound.play.PawnboundServerDisconnectPacket;
import dev.isxander.splitscreen.client.ipc.packets.pawnbound.play.PawnboundUseControllerPacket;
import dev.isxander.splitscreen.client.remote.RemotePawnMain;
import net.minecraft.class_2535;
import net.minecraft.class_2539;
import net.minecraft.class_2600;
import net.minecraft.class_310;
import net.minecraft.class_8697;
import net.minecraft.class_9812;
import org.slf4j.Logger;

public class PawnPlayPacketListener implements PawnboundCommonPacketListener, class_8697 {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final RemotePawnMain remotePawnMain;
   private final LocalSplitscreenPawn pawn;
   private final class_2535 connection;
   private final class_310 minecraft;

   public PawnPlayPacketListener(class_2535 connection, RemotePawnMain remotePawnMain, class_310 minecraft) {
      this.remotePawnMain = remotePawnMain;
      this.pawn = remotePawnMain.getPawn();
      this.connection = connection;
      this.minecraft = minecraft;
   }

   public void handleJoinServer(PawnboundJoinServerPacket packet) {
      class_2600.method_11074(packet, this, this.minecraft);
      LOGGER.info("Pawn joining server server {}:{}", packet.host(), packet.port());
      this.pawn.joinServer(packet.host(), packet.port(), (byte[])packet.nonce().orElse((Object)null));
   }

   public void handleServerDisconnect(PawnboundServerDisconnectPacket packet) {
      class_2600.method_11074(packet, this, this.minecraft);
      LOGGER.info("Pawn disconnecting from server");
      this.pawn.disconnectFromServer();
   }

   public void handleEngineCustomPayload(PawnboundEngineCustomPayloadPacket packet) {
      this.remotePawnMain.getSplitscreenEngine().handleInboundPayload(packet.payload());
   }

   public void handleCloseGame(PawnboundCloseGamePacket packet) {
      this.pawn.closeGame();
   }

   public void handleUseController(PawnboundUseControllerPacket packet) {
      class_2600.method_11074(packet, this, this.minecraft);
      LOGGER.info("Pawn using controller {}", packet.controllerUID());
      this.pawn.useController(packet.controllerUID());
   }

   public void handleKeepAlive(PawnboundKeepAlivePacket packet) {
      this.connection.method_10743(ControllerboundKeepAlivePacket.INSTANCE);
   }

   public void handleLoadConfig(PawnboundLoadConfigPacket packet) {
      class_2600.method_11074(packet, this, this.minecraft);
      this.pawn.onConfigSave(packet.config());
   }

   public void handleDisconnect(PawnboundDisconnectPacket packet) {
      System.out.println("Disconnecting from server: " + packet.reason().getString());
      this.connection.method_10747(packet.reason());
   }

   public class_2539 method_52280() {
      return class_2539.field_20591;
   }

   public void method_10839(class_9812 details) {
      System.out.println("Disconnecting from server2: " + details.comp_2853().getString());
      this.minecraft.method_1592();
   }

   public boolean method_48106() {
      return this.connection.method_10758();
   }
}
