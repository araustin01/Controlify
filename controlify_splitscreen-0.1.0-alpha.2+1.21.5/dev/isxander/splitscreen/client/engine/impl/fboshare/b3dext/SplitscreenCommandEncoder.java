package dev.isxander.splitscreen.client.engine.impl.fboshare.b3dext;

import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.TextureFormat;

public interface SplitscreenCommandEncoder {
   SplitscreenCommandEncoder UNSUPPORTED = new SplitscreenCommandEncoder() {
      public GpuTexture importTexture(ShareHandle handle, int width, int height, TextureFormat format) {
         throw new UnsupportedOperationException();
      }

      public ShareHandle shareTexture(GpuTexture texture) {
         throw new UnsupportedOperationException();
      }

      public TimelineSemaphore createSemaphore(long initialValue) {
         throw new UnsupportedOperationException();
      }

      public TimelineSemaphore importSemaphore(ShareHandle handle) {
         throw new UnsupportedOperationException();
      }

      public ShareHandle shareSemaphore(TimelineSemaphore semaphore) {
         throw new UnsupportedOperationException();
      }
   };

   GpuTexture importTexture(ShareHandle var1, int var2, int var3, TextureFormat var4);

   ShareHandle shareTexture(GpuTexture var1);

   TimelineSemaphore createSemaphore(long var1);

   TimelineSemaphore importSemaphore(ShareHandle var1);

   ShareHandle shareSemaphore(TimelineSemaphore var1);
}
