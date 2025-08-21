package dev.isxander.splitscreen.client.engine.impl.reparenting;

import com.mojang.logging.LogUtils;
import dev.isxander.splitscreen.client.LocalSplitscreenPawn;
import dev.isxander.splitscreen.client.engine.RemoteSplitscreenEngine;
import dev.isxander.splitscreen.client.engine.SplitscreenEnginePayloadSender;
import dev.isxander.splitscreen.client.engine.impl.reparenting.events.VanillaWindowFocusEvent;
import dev.isxander.splitscreen.client.engine.impl.reparenting.ipc.ControllerboundTakeFocusPayload;
import dev.isxander.splitscreen.client.engine.impl.reparenting.ipc.ControllerboundThisIsMyWindowPayload;
import dev.isxander.splitscreen.client.engine.impl.reparenting.ipc.PawnboundSetWindowActivePayload;
import dev.isxander.splitscreen.client.engine.impl.reparenting.ipc.PawnboundThrottleFrameratePayload;
import dev.isxander.splitscreen.client.engine.impl.reparenting.wm.NativeWindowHandle;
import dev.isxander.splitscreen.client.engine.impl.reparenting.wm.WindowManager;
import dev.isxander.splitscreen.client.remote.RemotePawnMain;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.class_1041;
import net.minecraft.class_310;
import net.minecraft.class_8710;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class ReparentingRemoteSplitscreenEngine extends ReparentingSplitscreenEngine implements RemoteSplitscreenEngine {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final class_310 minecraft;
   private final WindowManager windowManager;
   private final SplitscreenEnginePayloadSender payloadSender;
   private final LocalSplitscreenPawn localMainPawn;
   @Nullable
   private LocalReparentingPawn localPawn;

   public ReparentingRemoteSplitscreenEngine(class_310 minecraft, SplitscreenEnginePayloadSender payloadSender, LocalSplitscreenPawn localMainPawn) {
      this.minecraft = minecraft;
      this.windowManager = WindowManager.get();
      this.payloadSender = payloadSender;
      this.localMainPawn = localMainPawn;
      VanillaWindowFocusEvent.EVENT.register((window, focused) -> {
         if (focused) {
            this.giveFocusToController();
         }

      });
   }

   public boolean shouldThrottleFps() {
      return this.localPawn != null && this.localPawn.shouldThrottleFramerate();
   }

   public static Optional<ReparentingRemoteSplitscreenEngine> tryGet(RemotePawnMain remotePawnMain) {
      RemoteSplitscreenEngine var2 = remotePawnMain.getSplitscreenEngine();
      if (var2 instanceof ReparentingRemoteSplitscreenEngine) {
         ReparentingRemoteSplitscreenEngine engine = (ReparentingRemoteSplitscreenEngine)var2;
         return Optional.of(engine);
      } else {
         return Optional.empty();
      }
   }

   public void onWindowInit() {
      class_1041 window = this.minecraft.method_22683();
      NativeWindowHandle nativeHandle = this.windowManager.getNativeWindowHandle(window.method_4490());
      this.localPawn = new LocalReparentingPawn(this.minecraft, nativeHandle);
      this.payloadSender.sendPayload(new ControllerboundThisIsMyWindowPayload(nativeHandle));
   }

   public void handleInboundPayload(class_8710 payload) {
      if (this.localPawn == null) {
         throw new IllegalStateException("Local pawn not initialized");
      } else {
         Objects.requireNonNull(payload);
         byte var3 = 0;
         switch(payload.typeSwitch<invokedynamic>(payload, var3)) {
         case 0:
            PawnboundSetWindowActivePayload p = (PawnboundSetWindowActivePayload)payload;
            this.onPayloadSetWindowActive(this.localPawn, p);
            break;
         case 1:
            PawnboundThrottleFrameratePayload p = (PawnboundThrottleFrameratePayload)payload;
            this.onThrottleFramerate(this.localPawn, p);
            break;
         default:
            LOGGER.error("Unknown payload type: {}", payload.getClass().getName());
         }

      }
   }

   private void giveFocusToController() {
      this.payloadSender.sendPayload(new ControllerboundTakeFocusPayload());
      this.localPawn.setWindowFocusState(true);
   }

   private void onPayloadSetWindowActive(@NotNull LocalReparentingPawn localPawn, PawnboundSetWindowActivePayload payload) {
      localPawn.setWindowFocusState(payload.active());
   }

   private void onThrottleFramerate(@NotNull LocalReparentingPawn localPawn, PawnboundThrottleFrameratePayload payload) {
      localPawn.setThrottleFramerate(payload.throttle());
   }
}
