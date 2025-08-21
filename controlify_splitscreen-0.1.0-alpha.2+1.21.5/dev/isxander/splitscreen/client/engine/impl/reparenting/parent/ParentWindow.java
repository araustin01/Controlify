package dev.isxander.splitscreen.client.engine.impl.reparenting.parent;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import dev.isxander.splitscreen.client.engine.impl.reparenting.wm.NativeWindowHandle;
import dev.isxander.splitscreen.client.engine.impl.reparenting.wm.WindowManager;
import dev.isxander.splitscreen.client.mixins.engine.reparent.ScreenManagerAccessor;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.class_1011;
import net.minecraft.class_310;
import net.minecraft.class_313;
import net.minecraft.class_319;
import net.minecraft.class_323;
import net.minecraft.class_3262;
import net.minecraft.class_3532;
import net.minecraft.class_543;
import net.minecraft.class_7367;
import net.minecraft.class_8518;
import net.minecraft.class_1011.class_1012;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWImage.Buffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;

public class ParentWindow implements AutoCloseable {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final class_323 screenManager;
   private final class_310 minecraft;
   private final ParentWindowEventHandler eventHandler;
   private final long glfwWindowHandle;
   private int x;
   private int y;
   private int windowedX;
   private int windowedY;
   private int width;
   private int height;
   private int windowedWidth;
   private int windowedHeight;
   private boolean fullscreen;
   private boolean actualFullscreen;

   public ParentWindow(class_310 minecraft, class_543 screenSize, class_323 screenManager, ParentWindowEventHandler eventHandler, String initialTitle) {
      this.screenManager = screenManager;
      this.minecraft = minecraft;
      this.eventHandler = eventHandler;
      class_313 monitor = screenManager.method_1680(GLFW.glfwGetPrimaryMonitor());
      this.width = this.windowedWidth = Math.max(screenSize.comp_3494(), 1);
      this.height = this.windowedHeight = Math.max(screenSize.comp_3495(), 1);
      this.fullscreen = screenSize.comp_3498();
      this.actualFullscreen = false;
      GLFW.glfwDefaultWindowHints();
      this.glfwWindowHandle = GLFW.glfwCreateWindow(this.width, this.height, initialTitle, 0L, 0L);
      if (this.glfwWindowHandle == 0L) {
         handleLastGLFWError((errorCode, description) -> {
            throw new RuntimeException("Failed to create GLFW window: " + description, new Throwable());
         });
      }

      GLFW.glfwSetWindowFocusCallback(this.glfwWindowHandle, this::windowFocusCallback);
      GLFW.glfwSetWindowSizeCallback(this.glfwWindowHandle, this::windowPosCallback);
      GLFW.glfwSetWindowSizeCallback(this.glfwWindowHandle, this::windowSizeCallback);
      int[] xBuffer = new int[1];
      int[] yBuffer = new int[1];
      GLFW.glfwGetWindowPos(this.glfwWindowHandle, xBuffer, yBuffer);
      this.x = this.windowedX = xBuffer[0];
      this.y = this.windowedY = yBuffer[0];
      this.updateFullscreen();
   }

   public long getGlfwWindowHandle() {
      return this.glfwWindowHandle;
   }

   public NativeWindowHandle getNativeWindowHandle() {
      return WindowManager.get().getNativeWindowHandle(this.getGlfwWindowHandle());
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public boolean isFullscreen() {
      return this.fullscreen;
   }

   public void setWindowed(int width, int height) {
      this.width = width;
      this.height = height;
      this.fullscreen = false;
      this.updateFullscreen();
      this.flushWindowDimensions();
   }

   public void setFullscreen(boolean fullscreen) {
      this.fullscreen = fullscreen;
      this.updateFullscreen();
   }

   public void toggleFullscreen() {
      this.fullscreen = !this.fullscreen;
      this.updateFullscreen();
   }

   private void updateFullscreen() {
      if (this.fullscreen != this.actualFullscreen) {
         WindowManager windowManager = WindowManager.get();
         if (this.fullscreen) {
            class_313 bestMonitor = this.findBestMonitor();
            if (bestMonitor == null) {
               LOGGER.error("Failed to fullscreen update: no best monitor found");
               this.fullscreen = false;
               return;
            }

            class_319 videoMode = bestMonitor.method_1617();
            this.windowedX = this.x;
            this.windowedY = this.y;
            this.windowedWidth = this.width;
            this.windowedHeight = this.height;
            this.x = 0;
            this.y = 0;
            this.width = videoMode.method_1668();
            this.height = videoMode.method_1669();
            this.actualFullscreen = true;
            windowManager.setBorderless(windowManager.getNativeWindowHandle(this.glfwWindowHandle), true, this.x, this.y, this.width, this.height);
         } else {
            this.x = this.windowedX;
            this.y = this.windowedY;
            this.width = this.windowedWidth;
            this.height = this.windowedHeight;
            this.actualFullscreen = false;
            windowManager.setBorderless(windowManager.getNativeWindowHandle(this.glfwWindowHandle), false, this.x, this.y, this.width, this.height);
         }
      }

   }

   private void flushWindowDimensions() {
      GLFW.glfwSetWindowPos(this.glfwWindowHandle, this.x, this.y);
      GLFW.glfwSetWindowSize(this.glfwWindowHandle, this.width, this.height);
   }

   public void setTitle(String title) {
      RenderSystem.assertOnRenderThread();
      GLFW.glfwSetWindowTitle(this.glfwWindowHandle, title + " - Controlify Splitscreen");
   }

   public void setIcon(class_3262 resources, class_8518 iconSet) {
      try {
         switch(GLFW.glfwGetPlatform()) {
         case 393217:
         case 393220:
            List<class_7367<InputStream>> icons = iconSet.method_51418(resources);
            ArrayList iconBuffers = new ArrayList(icons.size());

            try {
               MemoryStack stack = MemoryStack.stackPush();

               try {
                  Buffer imgBuffer = GLFWImage.malloc(icons.size(), stack);

                  for(int i = 0; i < icons.size(); ++i) {
                     class_1011 image = class_1011.method_4309((InputStream)((class_7367)icons.get(i)).get());

                     try {
                        ByteBuffer iconBuffer = MemoryUtil.memAlloc(image.method_4307() * image.method_4323() * class_1012.field_4997.method_4335());
                        iconBuffers.add(iconBuffer);
                        iconBuffer.asIntBuffer().put(image.method_48463());
                        imgBuffer.position(i);
                        imgBuffer.width(image.method_4307());
                        imgBuffer.height(image.method_4323());
                        imgBuffer.pixels(iconBuffer);
                     } catch (Throwable var20) {
                        if (image != null) {
                           try {
                              image.close();
                           } catch (Throwable var19) {
                              var20.addSuppressed(var19);
                           }
                        }

                        throw var20;
                     }

                     if (image != null) {
                        image.close();
                     }
                  }

                  imgBuffer.flip();
                  this.setIcon(imgBuffer);
               } catch (Throwable var21) {
                  if (stack != null) {
                     try {
                        stack.close();
                     } catch (Throwable var18) {
                        var21.addSuppressed(var18);
                     }
                  }

                  throw var21;
               }

               if (stack != null) {
                  stack.close();
               }
            } finally {
               iconBuffers.forEach(MemoryUtil::memFree);
            }
         case 393218:
         case 393219:
         }
      } catch (IOException var23) {
         LOGGER.error("Failed to set window icon", var23);
      }

   }

   public void setIcon(Buffer iconBuffer) {
      RenderSystem.assertOnRenderThread();
      iconBuffer.mark();
      iconBuffer.position(0);
      GLFW.glfwSetWindowIcon(this.glfwWindowHandle, iconBuffer);
      iconBuffer.reset();
   }

   public boolean shouldClose() {
      RenderSystem.assertOnRenderThread();
      return GLFW.glfwWindowShouldClose(this.glfwWindowHandle);
   }

   private void windowFocusCallback(long window, boolean focused) {
      if (window == this.glfwWindowHandle) {
         this.eventHandler.onFocusParentWindow(focused);
      }
   }

   private void windowPosCallback(long window, int x, int y) {
      if (window == this.glfwWindowHandle) {
         this.x = x;
         this.y = y;
      }
   }

   private void windowSizeCallback(long window, int width, int height) {
      if (window == this.glfwWindowHandle) {
         this.width = width;
         this.height = height;
         this.eventHandler.onResizeParentWindow(width, height);
      }
   }

   public void close() {
      RenderSystem.assertOnRenderThread();
      Callbacks.glfwFreeCallbacks(this.glfwWindowHandle);
      GLFW.glfwDestroyWindow(this.glfwWindowHandle);
   }

   @Nullable
   private class_313 findBestMonitor() {
      int windowLeftX = this.x;
      int windowRightX = windowLeftX + this.width;
      int windowTopY = this.y;
      int windowBottomY = windowTopY + this.height;
      int maxOverlapArea = -1;
      class_313 bestMonitor = null;
      long primaryMonitorHandle = GLFW.glfwGetPrimaryMonitor();
      ObjectIterator var9 = ((ScreenManagerAccessor)this.screenManager).getMonitors().values().iterator();

      while(var9.hasNext()) {
         class_313 monitor = (class_313)var9.next();
         int monitorLeftX = monitor.method_1616();
         int monitorRightX = monitorLeftX + monitor.method_1617().method_1668();
         int monitorTopY = monitor.method_1618();
         int monitorBottomY = monitorTopY + monitor.method_1617().method_1669();
         int clampedWindowLeftX = class_3532.method_15340(windowLeftX, monitorLeftX, monitorRightX);
         int clampedWindowRightX = class_3532.method_15340(windowRightX, monitorLeftX, monitorRightX);
         int clampedWindowTopY = class_3532.method_15340(windowTopY, monitorTopY, monitorBottomY);
         int clampedWindowBottomY = class_3532.method_15340(windowBottomY, monitorTopY, monitorBottomY);
         int overlapWidth = Math.max(0, clampedWindowRightX - clampedWindowLeftX);
         int overlapHeight = Math.max(0, clampedWindowBottomY - clampedWindowTopY);
         int overlapArea = overlapWidth * overlapHeight;
         if (overlapArea > maxOverlapArea) {
            maxOverlapArea = overlapArea;
            bestMonitor = monitor;
         } else if (overlapArea == maxOverlapArea && primaryMonitorHandle == monitor.method_1622()) {
            bestMonitor = monitor;
         }
      }

      return bestMonitor;
   }

   private static void handleLastGLFWError(BiConsumer<Integer, String> handler) {
      MemoryStack memorystack = MemoryStack.stackPush();

      try {
         PointerBuffer pointerbuffer = memorystack.mallocPointer(1);
         int error = GLFW.glfwGetError(pointerbuffer);
         if (error != 0) {
            long pDescription = pointerbuffer.get();
            String description = pDescription == 0L ? "" : MemoryUtil.memUTF8(pDescription);
            handler.accept(error, description);
         }
      } catch (Throwable var8) {
         if (memorystack != null) {
            try {
               memorystack.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (memorystack != null) {
         memorystack.close();
      }

   }
}
