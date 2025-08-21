package dev.isxander.splitscreen.client.engine.impl.fboshare.b3dext;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.textures.GpuTexture;

public interface TimelineSemaphore {
   void signal(long var1, GpuBuffer[] var3, GpuTexture[] var4);

   void acquire(long var1, GpuBuffer[] var3, GpuTexture[] var4, long var5);

   void close();
}
