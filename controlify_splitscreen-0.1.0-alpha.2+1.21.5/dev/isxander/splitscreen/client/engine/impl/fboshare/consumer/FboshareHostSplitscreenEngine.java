package dev.isxander.splitscreen.client.engine.impl.fboshare.consumer;

import dev.isxander.controlify.controller.ControllerUID;
import dev.isxander.splitscreen.client.SplitscreenPosition;
import dev.isxander.splitscreen.client.engine.HostSplitscreenEngine;
import dev.isxander.splitscreen.client.engine.impl.fboshare.FboshareSplitscreenEngine;
import dev.isxander.splitscreen.client.engine.impl.fboshare.ipc.ControllerboundShareMemoryPayload;
import java.util.Objects;
import net.minecraft.class_2535;
import net.minecraft.class_8710;

public class FboshareHostSplitscreenEngine extends FboshareSplitscreenEngine implements HostSplitscreenEngine {
   public void setSplitscreenMode(ControllerUID window, SplitscreenPosition position) {
   }

   public void removeWindow(ControllerUID window) {
   }

   public boolean isDirty() {
      return false;
   }

   public boolean consumeDirty() {
      return false;
   }

   public boolean shouldExit() {
      return false;
   }

   private void handleShareMemory(ControllerUID window, class_8710 payload) {
   }

   public void handleInboundPayload(ControllerUID window, class_2535 connection, class_8710 payload) {
      Objects.requireNonNull(payload);
      byte var5 = 0;
      switch(payload.typeSwitch<invokedynamic>(payload, var5)) {
      case 0:
         ControllerboundShareMemoryPayload shareMemoryPayload = (ControllerboundShareMemoryPayload)payload;
         return;
      default:
         throw new IllegalStateException("Unexpected value: " + String.valueOf(payload));
      }
   }
}
