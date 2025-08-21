package dev.isxander.splitscreen.client.engine.impl.reparenting.ipc;

import dev.isxander.splitscreen.client.engine.impl.reparenting.wm.NativeWindowHandle;
import dev.isxander.splitscreen.util.CSUtil;
import net.minecraft.class_2540;
import net.minecraft.class_8710;
import net.minecraft.class_9139;
import net.minecraft.class_8710.class_9154;

public record ControllerboundThisIsMyWindowPayload(NativeWindowHandle windowHandle) implements class_8710 {
   public static final class_9139<class_2540, ControllerboundThisIsMyWindowPayload> CODEC;
   public static final class_9154<ControllerboundThisIsMyWindowPayload> TYPE;

   public ControllerboundThisIsMyWindowPayload(NativeWindowHandle windowHandle) {
      this.windowHandle = windowHandle;
   }

   public class_9154<ControllerboundThisIsMyWindowPayload> method_56479() {
      return TYPE;
   }

   public NativeWindowHandle windowHandle() {
      return this.windowHandle;
   }

   static {
      CODEC = NativeWindowHandle.STREAM_CODEC.method_56432(ControllerboundThisIsMyWindowPayload::new, ControllerboundThisIsMyWindowPayload::windowHandle);
      TYPE = new class_9154(CSUtil.rl("this_is_my_window"));
   }
}
