package dev.isxander.splitscreen.client.engine;

import dev.isxander.controlify.controller.ControllerUID;
import dev.isxander.splitscreen.client.SplitscreenPosition;
import dev.isxander.splitscreen.client.engine.impl.reparenting.ReparentingHostSplitscreenEngine;
import net.minecraft.class_2535;
import net.minecraft.class_310;
import net.minecraft.class_8710;

public interface HostSplitscreenEngine extends SplitscreenEngine {
   static HostSplitscreenEngine create(class_310 minecraft, ControllerUID localController) {
      return new ReparentingHostSplitscreenEngine(minecraft, localController);
   }

   void setSplitscreenMode(ControllerUID var1, SplitscreenPosition var2);

   void removeWindow(ControllerUID var1);

   boolean isDirty();

   boolean consumeDirty();

   boolean shouldExit();

   void handleInboundPayload(ControllerUID var1, class_2535 var2, class_8710 var3);
}
