package dev.isxander.splitscreen.client.engine.impl.reparenting.wm;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import java.util.Objects;
import org.lwjgl.glfw.GLFWNativeWin32;

public class Win32WindowManager implements WindowManager {
   public static final Win32WindowManager INSTANCE = new Win32WindowManager();
   private static final User32 USER32;

   public NativeWindowHandle getNativeWindowHandle(long glfwHandle) {
      return new NativeWindowHandle(GLFWNativeWin32.glfwGetWin32Window(glfwHandle));
   }

   public void embedWindow(NativeWindowHandle parentHandle, NativeWindowHandle childHandle) {
      HWND childHwnd = hwnd(childHandle);
      HWND parentHwnd = hwnd(parentHandle);
      USER32.SetParent(childHwnd, parentHwnd);
      int style = 1342177280;
      USER32.SetWindowLong(childHwnd, -16, style);
      USER32.SetWindowPos(childHwnd, (HWND)null, 0, 0, 10, 10, 20);
      USER32.SetForegroundWindow(parentHwnd);
   }

   public boolean giveChildFocusIfParentIsForeground(NativeWindowHandle parentHandle, NativeWindowHandle childHandle) {
      HWND childHwnd = hwnd(childHandle);
      HWND parentHwnd = hwnd(parentHandle);
      HWND foregroundHwnd = USER32.GetForegroundWindow();
      if (Objects.equals(foregroundHwnd, parentHwnd)) {
         USER32.SetFocus(childHwnd);
         return true;
      } else {
         return false;
      }
   }

   public void setupWindowDims(NativeWindowHandle handle, int x, int y, int width, int height) {
      HWND windowHandle = hwnd(handle);
      USER32.SetWindowPos(windowHandle, (HWND)null, x, y, width, height, 84);
   }

   public void hideWindow(NativeWindowHandle handle) {
      HWND windowHandle = hwnd(handle);
      USER32.ShowWindow(windowHandle, 0);
   }

   public void setWindowForeground(NativeWindowHandle handle) {
      HWND windowHandle = hwnd(handle);
      USER32.SetForegroundWindow(windowHandle);
   }

   public void setWindowFocused(NativeWindowHandle handle) {
      HWND windowHandle = hwnd(handle);
      USER32.SetFocus(windowHandle);
   }

   public void setBorderless(NativeWindowHandle handle, boolean borderless, int x, int y, int width, int height) {
      HWND windowHandle = hwnd(handle);
      int style;
      int stylesToRemove;
      if (borderless) {
         style = USER32.GetWindowLong(windowHandle, -16);
         stylesToRemove = 13565952;
         int stylesToAdd = -1879048192;
         style = style & ~stylesToRemove | stylesToAdd;
         USER32.SetWindowLong(windowHandle, -16, style);
         int exStyle = USER32.GetWindowLong(windowHandle, -20);
         int exStylesToRemove = 131841;
         exStyle &= ~exStylesToRemove;
         USER32.SetWindowLong(windowHandle, -20, exStyle);
         USER32.SetWindowPos(windowHandle, new HWND(Pointer.createConstant(0L)), x, y, width, height, 112);
      } else {
         style = USER32.GetWindowLong(windowHandle, -16);
         style &= Integer.MAX_VALUE;
         style |= 282001408;
         USER32.SetWindowLong(windowHandle, -16, style);
         stylesToRemove = USER32.GetWindowLong(windowHandle, -20);
         stylesToRemove |= 256;
         USER32.SetWindowLong(windowHandle, -20, stylesToRemove);
         int flags = 96;
         USER32.SetWindowPos(windowHandle, new HWND(Pointer.createConstant(-2L)), x, y, width, height, flags);
      }

   }

   private static HWND hwnd(NativeWindowHandle handle) {
      return new HWND(new Pointer(handle.handle()));
   }

   static {
      USER32 = User32.INSTANCE;
   }
}
