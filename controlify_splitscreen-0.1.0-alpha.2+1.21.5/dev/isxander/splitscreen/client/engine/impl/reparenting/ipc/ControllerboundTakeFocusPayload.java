package dev.isxander.splitscreen.client.engine.impl.reparenting.ipc;

import dev.isxander.splitscreen.util.CSUtil;
import net.minecraft.class_2540;
import net.minecraft.class_8710;
import net.minecraft.class_9139;
import net.minecraft.class_8710.class_9154;

public record ControllerboundTakeFocusPayload() implements class_8710 {
   public static final ControllerboundTakeFocusPayload UNIT = new ControllerboundTakeFocusPayload();
   public static final class_9139<class_2540, ControllerboundTakeFocusPayload> STREAM_CODEC;
   public static final class_9154<ControllerboundTakeFocusPayload> TYPE;

   public class_9154<ControllerboundTakeFocusPayload> method_56479() {
      return TYPE;
   }

   static {
      STREAM_CODEC = class_9139.method_56431(UNIT);
      TYPE = new class_9154(CSUtil.rl("take_focus"));
   }
}
