package dev.isxander.splitscreen.client.host;

import com.mojang.logging.LogUtils;
import dev.isxander.controlify.controller.ControllerUID;
import dev.isxander.splitscreen.client.SplitscreenPawn;
import dev.isxander.splitscreen.client.SplitscreenPosition;
import dev.isxander.splitscreen.client.config.SplitscreenConfig;
import dev.isxander.splitscreen.client.engine.HostSplitscreenEngine;
import dev.isxander.splitscreen.client.features.screenop.ScreenSplitscreenMode;
import dev.isxander.splitscreen.client.features.screenop.ScreenSplitscreenModeRegistry;
import dev.isxander.splitscreen.client.host.features.music.PawnMusicManager;
import dev.isxander.splitscreen.client.host.features.relaunch.PendingRelaunchClientStatus;
import dev.isxander.splitscreen.client.host.features.relaunch.RelaunchProcessHandler;
import dev.isxander.splitscreen.client.host.gui.SplitscreenFakeReloadInstance;
import dev.isxander.splitscreen.client.host.gui.SplitscreenLoadingOverlay;
import dev.isxander.splitscreen.client.host.ipc.ControllerConnectionListener;
import dev.isxander.splitscreen.client.ipc.IPCMethod;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.class_310;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class SplitscreenController {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final class_310 minecraft;
   private final ControllerConnectionListener connectionListener;
   private final IPCMethod ipcMethod;
   private final List<SplitscreenPawn> pawns = new ArrayList();
   private final HostLocalSplitscreenPawn localPawn;
   private final LocalControllerBridge controllerBridge;
   private final HostSplitscreenEngine splitscreenEngine;
   private final PawnMusicManager pawnMusicManager;
   private final Map<ControllerUID, RelaunchProcessHandler> relaunchProcessHandlers = new HashMap();
   private final Map<ControllerUID, PendingRelaunchClientStatus> pendingRelaunchClients = new HashMap();
   @Nullable
   private SplitscreenFakeReloadInstance splitscreenLoaderStatus = null;

   public SplitscreenController(class_310 minecraft, IPCMethod ipcMethod, @Nullable ControllerUID associatedController) {
      this.minecraft = minecraft;
      this.controllerBridge = new LocalControllerBridge(minecraft, this);
      this.ipcMethod = ipcMethod;
      this.connectionListener = new ControllerConnectionListener(ipcMethod, this, minecraft);
      this.addPawn(this.localPawn = new HostLocalSplitscreenPawn(minecraft, associatedController));
      this.splitscreenEngine = HostSplitscreenEngine.create(this.minecraft, associatedController);
      this.pawnMusicManager = new PawnMusicManager();
      ClientTickEvents.START_CLIENT_TICK.register((client) -> {
         this.connectionListener.tick();
         if (this.splitscreenEngine.consumeDirty()) {
            this.updateSplitscreenMode();
         }

         if (this.splitscreenEngine.shouldExit()) {
            this.minecraft.method_1592();
         }

      });
   }

   public void forEachPawn(Consumer<SplitscreenPawn> consumer) {
      this.pawns.forEach(consumer);
   }

   public void forEachPawn(BiConsumer<SplitscreenPawn, Integer> consumer) {
      for(int i = 0; i < this.pawns.size(); ++i) {
         consumer.accept((SplitscreenPawn)this.pawns.get(i), i);
      }

   }

   public int getNextPawnIndex() {
      return this.pawns.size();
   }

   public void addPawn(SplitscreenPawn pawn) {
      int pawnIndex = this.getNextPawnIndex();
      if (pawn.pawnIndex() != pawnIndex) {
         throw new IllegalArgumentException("pawn's index does not match pawn list size. race condition?");
      } else {
         LOGGER.info("Adding pawn #{}", pawnIndex);
         this.pawns.add(pawn);
         ControllerUID associatedController = pawn.getAssociatedController();
         if (associatedController != null) {
            PendingRelaunchClientStatus newStatus = new PendingRelaunchClientStatus.WaitingForReadySignal(0.0F);
            PendingRelaunchClientStatus oldStatus = (PendingRelaunchClientStatus)this.pendingRelaunchClients.put(associatedController, newStatus);
            if (!(oldStatus instanceof PendingRelaunchClientStatus.WaitingForConnection)) {
               LOGGER.warn("Pawn connected with controller {} but we were not expecting it", associatedController);
            }
         }

      }
   }

   public void onPawnReadySignal(boolean finished, float progress, RemoteSplitscreenPawn pawn, @Nullable ControllerUID controllerUid) {
      if (controllerUid == null) {
         LOGGER.warn("Pawn ready signal received with no controller UID");
      } else {
         RelaunchProcessHandler process = (RelaunchProcessHandler)this.relaunchProcessHandlers.get(controllerUid);
         process.setPawn(pawn);
         if (finished) {
            this.pendingRelaunchClients.remove(controllerUid);
            LOGGER.info("Pawn {} is ready", controllerUid);
            this.updateSplitscreenMode();
         } else {
            PendingRelaunchClientStatus.WaitingForReadySignal newStatus = new PendingRelaunchClientStatus.WaitingForReadySignal(progress);
            PendingRelaunchClientStatus oldStatus = (PendingRelaunchClientStatus)this.pendingRelaunchClients.put(controllerUid, newStatus);
            if (oldStatus == null) {
               LOGGER.warn("Pawn {} sent ready update but we were not waiting on it", controllerUid);
            }
         }

      }
   }

   public void removePawn(SplitscreenPawn pawn) {
      this.pawns.remove(pawn);
      this.splitscreenEngine.removeWindow(pawn.getAssociatedController());
      this.splitscreenEngine.consumeDirty();
      this.updateSplitscreenMode();
   }

   public int getPawnCount(boolean includeLocal) {
      return this.pawns.size() - (includeLocal ? 0 : 1);
   }

   public HostLocalSplitscreenPawn getLocalPawn() {
      return this.localPawn;
   }

   public LocalControllerBridge getControllerBridge() {
      return this.controllerBridge;
   }

   public void setSplitscreenMode(ScreenSplitscreenMode mode) {
      if (this.minecraft.method_18506() != null) {
         mode = ScreenSplitscreenMode.FULLSCREEN;
      }

      switch(mode) {
      case FULLSCREEN:
         this.forEachPawn((pawn) -> {
            this.splitscreenEngine.setSplitscreenMode(pawn.getAssociatedController(), (SplitscreenPosition)(pawn == this.localPawn ? SplitscreenPosition.FULL : SplitscreenPosition.HIDDEN));
         });
         break;
      case SPLITSCREEN:
         int pawnCount = this.pawns.size();
         boolean horizontal = !(Boolean)SplitscreenConfig.INSTANCE.preferVerticalSplitscreen.get();
         SplitscreenPosition.Visible[] var10000;
         switch(pawnCount) {
         case 1:
            var10000 = new SplitscreenPosition.Visible[]{SplitscreenPosition.FULL};
            break;
         case 2:
            var10000 = horizontal ? SplitscreenPosition.LEFT_RIGHT : SplitscreenPosition.TOP_BOTTOM;
            break;
         case 3:
            var10000 = horizontal ? SplitscreenPosition.LEFT_TOP_BOTTOM : SplitscreenPosition.LEFT_RIGHT_BOTTOM;
            break;
         case 4:
            var10000 = SplitscreenPosition.FOUR_WAY;
            break;
         default:
            var10000 = SplitscreenPosition.Visible.arrangeInGridForN(pawnCount);
         }

         SplitscreenPosition.Visible[] positions = var10000;
         this.forEachPawn((pawn, i) -> {
            SplitscreenPosition position = positions[i];
            LOGGER.info("Setting pawn #{} to {}", i, position);
            this.splitscreenEngine.setSplitscreenMode(pawn.getAssociatedController(), position);
         });
      }

   }

   public void updateSplitscreenMode() {
      this.setSplitscreenMode(ScreenSplitscreenModeRegistry.getMode(this.minecraft.field_1755));
   }

   public PawnMusicManager getPawnMusicManager() {
      return this.pawnMusicManager;
   }

   public boolean summonNewPawnClient(ControllerUID controller) {
      if (this.relaunchProcessHandlers.containsKey(controller)) {
         return false;
      } else {
         int pawnIndex = this.pawns.size();
         RelaunchProcessHandler handler = RelaunchProcessHandler.createProcess(this.minecraft, controller, this, pawnIndex, this.ipcMethod);
         this.relaunchProcessHandlers.put(controller, handler);
         this.pendingRelaunchClients.put(controller, new PendingRelaunchClientStatus.WaitingForConnection());
         if (this.splitscreenLoaderStatus == null) {
            this.splitscreenLoaderStatus = new SplitscreenFakeReloadInstance(this.pendingRelaunchClients.values());
            if (this.minecraft.method_18506() != null) {
               LOGGER.error("Tried to open the splitscreen loading overlay but another overlay was open");
            } else {
               this.minecraft.method_18502(new SplitscreenLoadingOverlay(this.minecraft, this.splitscreenLoaderStatus, (err) -> {
                  this.splitscreenLoaderStatus = null;
               }, true));
            }
         }

         handler.onExit().whenComplete((h, throwable) -> {
            this.onRelaunchedPawnExit(controller, handler, throwable);
         });
         return true;
      }
   }

   private void onRelaunchedPawnExit(ControllerUID controller, RelaunchProcessHandler process, Throwable throwable) {
      if (this.pendingRelaunchClients.remove(controller) != null) {
         LOGGER.info("Relaunch client exited before it was ready, did it crash?");
      }

      if (this.relaunchProcessHandlers.remove(controller) != null) {
         LOGGER.info("Relaunch process for {} exited", controller);
      }

      SplitscreenPawn pawn = process.getPawn();
      if (pawn != null) {
         this.removePawn(pawn);
      }

   }

   public HostSplitscreenEngine getSplitscreenEngine() {
      return this.splitscreenEngine;
   }
}
