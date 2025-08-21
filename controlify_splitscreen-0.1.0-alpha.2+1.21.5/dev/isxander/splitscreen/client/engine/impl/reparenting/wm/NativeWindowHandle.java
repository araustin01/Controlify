package dev.isxander.splitscreen.client.engine.impl.reparenting.wm;

import net.minecraft.class_2540;
import net.minecraft.class_9135;
import net.minecraft.class_9139;

public record NativeWindowHandle(long handle) {
   public static final class_9139<class_2540, NativeWindowHandle> STREAM_CODEC;

   public NativeWindowHandle(long handle) {
      this.handle = handle;
   }

   public long handle() {
      return this.handle;
   }

   static {
      STREAM_CODEC = class_9135.field_54505.method_56439(class_2540::unwrap).method_56432(NativeWindowHandle::new, NativeWindowHandle::handle);
   }
}
