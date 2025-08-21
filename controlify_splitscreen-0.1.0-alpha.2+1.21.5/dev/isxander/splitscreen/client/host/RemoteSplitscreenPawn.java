package dev.isxander.splitscreen.client.host;

import dev.isxander.controlify.controller.ControllerUID;
import dev.isxander.splitscreen.client.SplitscreenPawn;
import dev.isxander.splitscreen.client.SplitscreenPosition;
import dev.isxander.splitscreen.client.ipc.packets.pawnbound.play.PawnboundCloseGamePacket;
import dev.isxander.splitscreen.client.ipc.packets.pawnbound.play.PawnboundJoinServerPacket;
import dev.isxander.splitscreen.client.ipc.packets.pawnbound.play.PawnboundLoadConfigPacket;
import dev.isxander.splitscreen.client.ipc.packets.pawnbound.play.PawnboundServerDisconnectPacket;
import dev.isxander.splitscreen.client.ipc.packets.pawnbound.play.PawnboundUseControllerPacket;
import java.util.Optional;
import net.minecraft.class_2535;
import net.minecraft.class_2960;
import org.jetbrains.annotations.Nullable;

public class RemoteSplitscreenPawn implements SplitscreenPawn {
   private final class_2535 connection;
   private SplitscreenPosition position = null;
   private final int index;
   @Nullable
   private final ControllerUID associatedController;

   public RemoteSplitscreenPawn(class_2535 connection, int index, @Nullable ControllerUID associatedController) {
      this.connection = connection;
      this.index = index;
      this.associatedController = associatedController;
   }

   public int pawnIndex() {
      return this.index;
   }

   public void joinServer(String serverAddress, int serverPort, @Nullable byte[] nonce) {
      this.connection.method_10743(new PawnboundJoinServerPacket(serverAddress, serverPort, Optional.ofNullable(nonce)));
   }

   public void closeGame() {
      this.connection.method_10743(new PawnboundCloseGamePacket());
   }

   public void disconnectFromServer() {
      this.connection.method_10743(PawnboundServerDisconnectPacket.UNIT);
   }

   public void useController(ControllerUID controllerUid) {
      this.connection.method_10743(new PawnboundUseControllerPacket(controllerUid));
   }

   public void onConfigSave(class_2960 config) {
      this.connection.method_10743(new PawnboundLoadConfigPacket(config));
   }

   public SplitscreenPosition getWindowSplitscreenMode() {
      return this.position;
   }

   @Nullable
   public ControllerUID getAssociatedController() {
      return this.associatedController;
   }

   public boolean isRemote() {
      return true;
   }

   public class_2535 getConnection() {
      return this.connection;
   }
}
