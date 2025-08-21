package dev.isxander.splitscreen.client.engine.impl.reparenting.ipc;

import dev.isxander.splitscreen.util.CSUtil;
import net.minecraft.class_2540;
import net.minecraft.class_8710;
import net.minecraft.class_9135;
import net.minecraft.class_9139;
import net.minecraft.class_8710.class_9154;

public record PawnboundSetWindowActivePayload(boolean active) implements class_8710 {
   public static final class_9139<class_2540, PawnboundSetWindowActivePayload> CODEC;
   public static final class_9154<PawnboundSetWindowActivePayload> TYPE;

   public PawnboundSetWindowActivePayload(boolean active) {
      this.active = active;
   }

   public class_9154<PawnboundSetWindowActivePayload> method_56479() {
      return TYPE;
   }

   public boolean active() {
      return this.active;
   }

   static {
      CODEC = class_9135.field_48547.method_56432(PawnboundSetWindowActivePayload::new, PawnboundSetWindowActivePayload::active).method_56439(class_2540::new);
      TYPE = new class_9154(CSUtil.rl("set_window_active"));
   }
}
