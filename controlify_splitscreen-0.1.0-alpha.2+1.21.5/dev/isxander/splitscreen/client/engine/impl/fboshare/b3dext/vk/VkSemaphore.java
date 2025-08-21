package dev.isxander.splitscreen.client.engine.impl.fboshare.b3dext.vk;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.textures.GpuTexture;
import dev.isxander.splitscreen.client.engine.impl.fboshare.b3dext.TimelineSemaphore;
import java.nio.LongBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK12;
import org.lwjgl.vulkan.VkAllocationCallbacks;
import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkSemaphoreSignalInfo;
import org.lwjgl.vulkan.VkSemaphoreWaitInfo;

public class VkSemaphore implements TimelineSemaphore {
   private final VkDevice device;
   private final long semaphore;

   public VkSemaphore(VkDevice device, long semaphore) {
      this.device = device;
      this.semaphore = semaphore;
   }

   public void signal(long value, GpuBuffer[] buffers, GpuTexture[] textures) {
      MemoryStack stack = MemoryStack.stackPush();

      try {
         VkSemaphoreSignalInfo info = VkSemaphoreSignalInfo.calloc(stack).sType(1000207005).pNext(0L).semaphore(this.semaphore).value(value);
         VkUtil.safeVkCall(() -> {
            return VK12.vkSignalSemaphore(this.device, info);
         }, () -> {
            return "failed to signal semaphore";
         });
      } catch (Throwable var9) {
         if (stack != null) {
            try {
               stack.close();
            } catch (Throwable var8) {
               var9.addSuppressed(var8);
            }
         }

         throw var9;
      }

      if (stack != null) {
         stack.close();
      }

   }

   public void acquire(long value, GpuBuffer[] buffers, GpuTexture[] textures, long timeoutNs) {
      MemoryStack stack = MemoryStack.stackPush();

      try {
         LongBuffer semBuf = stack.longs(this.semaphore);
         LongBuffer valBuf = stack.longs(value);
         VkSemaphoreWaitInfo info = VkSemaphoreWaitInfo.calloc(stack).sType(1000207004).pNext(0L).flags(0).pSemaphores(semBuf).pValues(valBuf);
         VkUtil.safeVkCall(() -> {
            return VK12.vkWaitSemaphores(this.device, info, timeoutNs);
         }, () -> {
            return "failed to wait for semaphore";
         });
      } catch (Throwable var12) {
         if (stack != null) {
            try {
               stack.close();
            } catch (Throwable var11) {
               var12.addSuppressed(var11);
            }
         }

         throw var12;
      }

      if (stack != null) {
         stack.close();
      }

   }

   public long getHandle() {
      return this.semaphore;
   }

   public void close() {
      VK12.vkDestroySemaphore(this.device, this.semaphore, (VkAllocationCallbacks)null);
   }
}
