package dev.isxander.splitscreen.client.engine.impl.fboshare.ipc;

import dev.isxander.splitscreen.client.engine.impl.fboshare.b3dext.ShareHandle;
import dev.isxander.splitscreen.client.engine.impl.fboshare.b3dext.SharedRenderTarget;
import dev.isxander.splitscreen.util.CSUtil;
import java.util.List;
import net.minecraft.class_2540;
import net.minecraft.class_8710;
import net.minecraft.class_9135;
import net.minecraft.class_9139;
import net.minecraft.class_8710.class_9154;

public record ControllerboundShareMemoryPayload(List<SharedRenderTarget> frameBuffers, ShareHandle semaphore) implements class_8710 {
   public static final class_9139<class_2540, ControllerboundShareMemoryPayload> CODEC;
   public static final class_9154<ControllerboundShareMemoryPayload> TYPE;

   public ControllerboundShareMemoryPayload(List<SharedRenderTarget> frameBuffers, ShareHandle semaphore) {
      this.frameBuffers = frameBuffers;
      this.semaphore = semaphore;
   }

   public class_9154<ControllerboundShareMemoryPayload> method_56479() {
      return TYPE;
   }

   public List<SharedRenderTarget> frameBuffers() {
      return this.frameBuffers;
   }

   public ShareHandle semaphore() {
      return this.semaphore;
   }

   static {
      CODEC = class_9139.method_56435(class_9135.method_56363().apply(SharedRenderTarget.STREAM_CODEC), ControllerboundShareMemoryPayload::frameBuffers, ShareHandle.CODEC, ControllerboundShareMemoryPayload::semaphore, ControllerboundShareMemoryPayload::new);
      TYPE = new class_9154(CSUtil.rl("share_memory"));
   }
}
