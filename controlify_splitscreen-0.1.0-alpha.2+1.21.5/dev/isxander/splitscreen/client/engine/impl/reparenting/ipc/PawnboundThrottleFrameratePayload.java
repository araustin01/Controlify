package dev.isxander.splitscreen.client.engine.impl.reparenting.ipc;

import dev.isxander.splitscreen.util.CSUtil;
import net.minecraft.class_2540;
import net.minecraft.class_8710;
import net.minecraft.class_9135;
import net.minecraft.class_9139;
import net.minecraft.class_8710.class_9154;

public record PawnboundThrottleFrameratePayload(boolean throttle) implements class_8710 {
   public static final class_9139<class_2540, PawnboundThrottleFrameratePayload> CODEC;
   public static final class_9154<PawnboundThrottleFrameratePayload> TYPE;

   public PawnboundThrottleFrameratePayload(boolean throttle) {
      this.throttle = throttle;
   }

   public class_9154<PawnboundThrottleFrameratePayload> method_56479() {
      return TYPE;
   }

   public boolean throttle() {
      return this.throttle;
   }

   static {
      CODEC = class_9135.field_48547.method_56432(PawnboundThrottleFrameratePayload::new, PawnboundThrottleFrameratePayload::throttle).method_56439(class_2540::new);
      TYPE = new class_9154(CSUtil.rl("throttle_framerate"));
   }
}
