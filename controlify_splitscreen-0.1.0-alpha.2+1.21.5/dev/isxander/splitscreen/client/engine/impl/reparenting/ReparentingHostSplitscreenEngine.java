package dev.isxander.splitscreen.client.engine.impl.reparenting;

import com.mojang.logging.LogUtils;
import dev.isxander.controlify.controller.ControllerUID;
import dev.isxander.splitscreen.client.SplitscreenPosition;
import dev.isxander.splitscreen.client.engine.HostSplitscreenEngine;
import dev.isxander.splitscreen.client.engine.SplitscreenEnginePayloadSender;
import dev.isxander.splitscreen.client.engine.impl.reparenting.ipc.ControllerboundTakeFocusPayload;
import dev.isxander.splitscreen.client.engine.impl.reparenting.ipc.ControllerboundThisIsMyWindowPayload;
import dev.isxander.splitscreen.client.engine.impl.reparenting.parent.ParentWindow;
import dev.isxander.splitscreen.client.engine.impl.reparenting.parent.ParentWindowEventHandler;
import dev.isxander.splitscreen.client.engine.impl.reparenting.wm.NativeWindowHandle;
import dev.isxander.splitscreen.client.engine.impl.reparenting.wm.WindowManager;
import dev.isxander.splitscreen.client.host.SplitscreenController;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Consumer;
import net.minecraft.class_155;
import net.minecraft.class_2535;
import net.minecraft.class_310;
import net.minecraft.class_323;
import net.minecraft.class_543;
import net.minecraft.class_8030;
import net.minecraft.class_8518;
import net.minecraft.class_8710;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class ReparentingHostSplitscreenEngine extends ReparentingSplitscreenEngine implements HostSplitscreenEngine, ParentWindowEventHandler {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final class_310 minecraft;
   private final ControllerUID localController;
   private final WindowManager windowManager;
   @Nullable
   private ParentWindow parentWindow;
   private final Queue<Consumer<ParentWindow>> pendingWindowTasks = new ArrayDeque();
   private boolean dirty = false;
   private LocalReparentingPawn localPawn;
   private final Map<ControllerUID, ReparentingPawn> pawns = new HashMap();

   public ReparentingHostSplitscreenEngine(class_310 minecraft, ControllerUID localController) {
      this.minecraft = minecraft;
      this.windowManager = WindowManager.get();
      this.localController = localController;
   }

   public static Optional<ReparentingHostSplitscreenEngine> tryGet(SplitscreenController controller) {
      HostSplitscreenEngine var2 = controller.getSplitscreenEngine();
      if (var2 instanceof ReparentingHostSplitscreenEngine) {
         ReparentingHostSplitscreenEngine reparenting = (ReparentingHostSplitscreenEngine)var2;
         return Optional.of(reparenting);
      } else {
         return Optional.empty();
      }
   }

   public void initWindow(class_543 screenSize, class_323 screenManager, String initialTitle) {
      if (this.parentWindow != null) {
         LOGGER.warn("Parent window already initialized, skipping");
      } else {
         LOGGER.info("Setting up parent window");
         this.parentWindow = new ParentWindow(this.minecraft, screenSize, screenManager, this, initialTitle);
         this.localPawn = new LocalReparentingPawn(this.minecraft, this.windowManager.getNativeWindowHandle(this.minecraft.method_22683().method_4490()));
         this.registerPawn(this.localController, this.localPawn);
         this.parentWindow.setIcon(this.minecraft.method_45573(), class_155.method_16673().method_48022() ? class_8518.field_44650 : class_8518.field_44651);

         while(!this.pendingWindowTasks.isEmpty()) {
            Consumer<ParentWindow> task = (Consumer)this.pendingWindowTasks.poll();
            if (task != null) {
               task.accept(this.parentWindow);
            }
         }

      }
   }

   public void registerPawn(ControllerUID window, ReparentingPawn pawn) {
      if (this.parentWindow == null) {
         throw new IllegalStateException("Parent window not yet initialised when remote pawn has already registered.");
      } else {
         this.pawns.put(window, pawn);
         this.windowManager.embedWindow(this.parentWindow.getNativeWindowHandle(), pawn.getNativeWindowHandle());
         this.windowManager.setWindowForeground(this.parentWindow.getNativeWindowHandle());
         this.windowManager.setWindowFocused(pawn.getNativeWindowHandle());
         Iterator var3 = this.pawns.values().iterator();

         while(var3.hasNext()) {
            ReparentingPawn p = (ReparentingPawn)var3.next();
            p.setWindowFocusState(true);
         }

         this.setDirty();
      }
   }

   public void registerRemotePawn(ControllerUID window, class_2535 connection, ControllerboundThisIsMyWindowPayload payload) {
      if (this.parentWindow == null) {
         throw new IllegalStateException("Parent window not yet initialised when remote pawn has already registered.");
      } else {
         SplitscreenEnginePayloadSender sender = SplitscreenEnginePayloadSender.pawnbound(connection);
         RemoteReparentingPawn pawn = new RemoteReparentingPawn(sender, payload.windowHandle());
         this.registerPawn(window, pawn);
      }
   }

   public void setSplitscreenMode(ControllerUID window, SplitscreenPosition position) {
      ReparentingPawn pawn = this.getPawn(window);
      if (pawn == null) {
         LOGGER.warn("Tried to set splitscreen mode for a non-existent window {}", window);
      } else {
         NativeWindowHandle windowHandle = pawn.getNativeWindowHandle();
         Objects.requireNonNull(position);
         byte var6 = 0;
         switch(position.typeSwitch<invokedynamic>(position, var6)) {
         case 0:
            SplitscreenPosition.Visible visible = (SplitscreenPosition.Visible)position;
            class_8030 windowDims = visible.applyToRealDims(0, 0, this.parentWindow.getWidth(), this.parentWindow.getHeight());
            this.windowManager.setupWindowDims(windowHandle, windowDims.method_49620(), windowDims.method_49618(), windowDims.comp_1196(), windowDims.comp_1197());
            break;
         case 1:
            SplitscreenPosition.Hidden ignored = (SplitscreenPosition.Hidden)position;
            this.windowManager.hideWindow(windowHandle);
            break;
         default:
            throw new MatchException((String)null, (Throwable)null);
         }

         pawn.setThrottleFramerate(position instanceof SplitscreenPosition.Hidden);
      }
   }

   public void removeWindow(ControllerUID window) {
      this.setDirty();
      this.pawns.remove(window);
   }

   @Nullable
   public ParentWindow getParentWindow() {
      return this.parentWindow;
   }

   public boolean consumeDirty() {
      boolean wasDirty = this.dirty;
      this.dirty = false;
      return wasDirty;
   }

   public boolean isDirty() {
      return this.dirty;
   }

   private void setDirty() {
      this.dirty = true;
   }

   public boolean shouldExit() {
      if (this.parentWindow == null) {
         return false;
      } else if (this.parentWindow.shouldClose()) {
         this.parentWindow.close();
         this.parentWindow = null;
         return true;
      } else {
         return false;
      }
   }

   public void onResizeParentWindow(int width, int height) {
      this.setDirty();
   }

   public void onFocusParentWindow(boolean focused) {
      if (focused) {
         this.windowManager.setWindowForeground(this.parentWindow.getNativeWindowHandle());
         this.windowManager.setWindowFocused(this.windowManager.getNativeWindowHandle(this.minecraft.method_22683().method_4490()));
      }

   }

   public void onOtherClientGotFocus(ControllerUID window) {
      this.onFocusParentWindow(true);
      Iterator var2 = this.pawns.values().iterator();

      while(var2.hasNext()) {
         ReparentingPawn p = (ReparentingPawn)var2.next();
         p.setWindowFocusState(true);
      }

   }

   public void handleInboundPayload(ControllerUID window, class_2535 connection, class_8710 payload) {
      Objects.requireNonNull(payload);
      byte var5 = 0;
      switch(payload.typeSwitch<invokedynamic>(payload, var5)) {
      case 0:
         ControllerboundThisIsMyWindowPayload registerPayload = (ControllerboundThisIsMyWindowPayload)payload;
         this.registerRemotePawn(window, connection, registerPayload);
         break;
      case 1:
         ControllerboundTakeFocusPayload takeFocusPayload = (ControllerboundTakeFocusPayload)payload;
         this.onOtherClientGotFocus(window);
         break;
      default:
         LOGGER.warn("Received unknown payload {}", payload.getClass().getSimpleName());
      }

   }

   @Nullable
   private ReparentingPawn getPawn(ControllerUID window) {
      return (ReparentingPawn)this.pawns.get(window);
   }

   private void executeWhenWindowReady(Consumer<ParentWindow> task) {
      if (this.parentWindow != null) {
         task.accept(this.parentWindow);
      } else {
         this.pendingWindowTasks.add(task);
      }

   }
}
