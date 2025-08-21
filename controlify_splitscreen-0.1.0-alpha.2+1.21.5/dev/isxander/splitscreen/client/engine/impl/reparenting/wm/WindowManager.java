package dev.isxander.splitscreen.client.engine.impl.reparenting.wm;

public interface WindowManager {
   static WindowManager get() {
      switch(dev.isxander.controlify.utils.WindowManager.INSTANCE) {
      case WIN32:
         return Win32WindowManager.INSTANCE;
      case X11:
         throw new UnsupportedWindowManagerException("X11 is not supported");
      case WAYLAND:
         throw new UnsupportedWindowManagerException("Wayland is unsupported for Controlify splitscreen");
      case COCOA:
         throw new UnsupportedWindowManagerException("macOS is unsupported for Controlify splitscreen");
      default:
         throw new UnsupportedWindowManagerException("Unknown platform, cannot embed window");
      }
   }

   NativeWindowHandle getNativeWindowHandle(long var1);

   void embedWindow(NativeWindowHandle var1, NativeWindowHandle var2);

   boolean giveChildFocusIfParentIsForeground(NativeWindowHandle var1, NativeWindowHandle var2);

   void setupWindowDims(NativeWindowHandle var1, int var2, int var3, int var4, int var5);

   void hideWindow(NativeWindowHandle var1);

   void setWindowForeground(NativeWindowHandle var1);

   void setWindowFocused(NativeWindowHandle var1);

   void setBorderless(NativeWindowHandle var1, boolean var2, int var3, int var4, int var5, int var6);
}
