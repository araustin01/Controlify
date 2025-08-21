package dev.isxander.splitscreen.client.host;

import dev.isxander.controlify.controller.ControllerUID;
import dev.isxander.splitscreen.client.ControllerBridge;
import dev.isxander.splitscreen.client.SplitscreenPawn;
import dev.isxander.splitscreen.client.host.gui.SplitscreenDisconnectedScreen;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_5195;
import org.jetbrains.annotations.Nullable;

public class LocalControllerBridge implements ControllerBridge {
   private final class_310 minecraft;
   private final SplitscreenController controller;

   public LocalControllerBridge(class_310 minecraft, SplitscreenController controller) {
      this.minecraft = minecraft;
      this.controller = controller;
   }

   public void giveFocusToMeIfForeground() {
   }

   public void signalImReady(boolean finished, float progress) {
   }

   public void serverDisconnected(class_2561 reason) {
      this.serverDisconnectedRemote(reason, (RemoteSplitscreenPawn)null);
   }

   public void serverDisconnectedRemote(class_2561 reason, RemoteSplitscreenPawn causePawn) {
      this.controller.forEachPawn((pawn) -> {
         pawn.disconnectFromServer();
         if (pawn instanceof HostLocalSplitscreenPawn) {
            class_2561 title = causePawn != null ? class_2561.method_43469("controlify.splitscreen.disconnect.remote_pawn", new Object[]{causePawn.pawnIndex()}) : class_2561.method_43471("controlify.splitscreen.disconnect.host_pawn");
            this.minecraft.method_1507(new SplitscreenDisconnectedScreen(this.minecraft.field_1755, title, reason));
         }

      });
   }

   public void signalRemoteClientReady(boolean finished, float progress, RemoteSplitscreenPawn pawn, @Nullable ControllerUID associatedController) {
      this.controller.onPawnReadySignal(finished, progress, pawn, associatedController);
   }

   public void requestPlayMusic(@Nullable class_5195 music, float volume) {
      this.requestPlayMusicRemote(music, volume, this.controller.getLocalPawn());
   }

   public void requestPlayMusicRemote(@Nullable class_5195 music, float volume, SplitscreenPawn pawn) {
      this.controller.getPawnMusicManager().onRequest(music, volume, pawn);
   }

   public boolean isRemote() {
      return false;
   }
}
