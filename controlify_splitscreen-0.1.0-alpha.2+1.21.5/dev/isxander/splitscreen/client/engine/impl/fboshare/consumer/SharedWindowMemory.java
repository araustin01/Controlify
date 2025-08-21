package dev.isxander.splitscreen.client.engine.impl.fboshare.consumer;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.textures.GpuTexture;
import dev.isxander.splitscreen.client.engine.impl.fboshare.b3dext.SharedRenderTarget;
import dev.isxander.splitscreen.client.engine.impl.fboshare.b3dext.SharedTexture;
import dev.isxander.splitscreen.client.engine.impl.fboshare.b3dext.SplitscreenCommandEncoder;
import dev.isxander.splitscreen.client.engine.impl.fboshare.b3dext.TimelineSemaphore;
import net.minecraft.class_276;

public final class SharedWindowMemory {
   private int currentFrame;
   private final class_276[] frames;
   private final TimelineSemaphore semaphore;

   public SharedWindowMemory(int currentFrame, class_276[] frames, TimelineSemaphore semaphore) {
      this.currentFrame = currentFrame;
      this.frames = frames;
      this.semaphore = semaphore;
   }

   public class_276 waitForNextFrame() {
      this.currentFrame = (this.currentFrame() + 1) % this.ringSize();
      class_276 frame = this.frames[this.currentFrame];
      this.semaphore().acquire((long)this.currentFrame, new GpuBuffer[0], new GpuTexture[]{frame.method_30277(), frame.method_30278()}, 100L);
      return frame;
   }

   public int currentFrame() {
      return this.currentFrame;
   }

   public class_276[] frames() {
      return this.frames;
   }

   public int ringSize() {
      return this.frames.length;
   }

   public TimelineSemaphore semaphore() {
      return this.semaphore;
   }

   public static class_276 importRenderTarget(SharedRenderTarget sharedRenderTarget) {
      SplitscreenCommandEncoder encoder = SplitscreenCommandEncoder.UNSUPPORTED;
      SharedTexture sColourTex = sharedRenderTarget.colour();
      encoder.importTexture(sColourTex.handle(), sharedRenderTarget.width(), sharedRenderTarget.height(), sColourTex.format());
      SharedTexture sDepthTex = sharedRenderTarget.depth();
      encoder.importTexture(sDepthTex.handle(), sharedRenderTarget.width(), sharedRenderTarget.height(), sDepthTex.format());
      return null;
   }
}
