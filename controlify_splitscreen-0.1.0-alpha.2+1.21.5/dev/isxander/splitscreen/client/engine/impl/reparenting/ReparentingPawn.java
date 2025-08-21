package dev.isxander.splitscreen.client.engine.impl.reparenting;

import dev.isxander.splitscreen.client.engine.impl.reparenting.wm.NativeWindowHandle;

public interface ReparentingPawn {
   NativeWindowHandle getNativeWindowHandle();

   void setWindowFocusState(boolean var1);

   void setThrottleFramerate(boolean var1);
}
