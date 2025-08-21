package dev.isxander.splitscreen.client.engine.impl.reparenting;

import dev.isxander.splitscreen.client.engine.impl.reparenting.wm.NativeWindowHandle;
import net.minecraft.class_310;

public class LocalReparentingPawn implements ReparentingPawn {
   private final class_310 minecraft;
   private final NativeWindowHandle windowHandle;
   private boolean throttleFramerate = false;

   public LocalReparentingPawn(class_310 minecraft, NativeWindowHandle windowHandle) {
      this.minecraft = minecraft;
      this.windowHandle = windowHandle;
   }

   public void setWindowFocusState(boolean active) {
      this.minecraft.method_15995(active);
   }

   public void setThrottleFramerate(boolean throttleFramerate) {
      this.throttleFramerate = throttleFramerate;
   }

   public boolean shouldThrottleFramerate() {
      return this.throttleFramerate;
   }

   public NativeWindowHandle getNativeWindowHandle() {
      return this.windowHandle;
   }
}
