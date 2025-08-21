package dev.isxander.splitscreen.server.play.sound;

import dev.isxander.splitscreen.util.CSUtil;
import net.minecraft.class_2540;
import net.minecraft.class_8710;
import net.minecraft.class_9135;
import net.minecraft.class_9139;
import net.minecraft.class_8710.class_9154;
import org.jetbrains.annotations.NotNull;

public record ClientboundSetBundleStatePacket(boolean sound) implements class_8710 {
   public static final class_9139<class_2540, ClientboundSetBundleStatePacket> STREAM_CODEC;
   public static final class_9154<ClientboundSetBundleStatePacket> TYPE;

   public ClientboundSetBundleStatePacket(boolean sound) {
      this.sound = sound;
   }

   @NotNull
   public class_9154<ClientboundSetBundleStatePacket> method_56479() {
      return TYPE;
   }

   public boolean sound() {
      return this.sound;
   }

   static {
      STREAM_CODEC = class_9139.method_56434(class_9135.field_48547, ClientboundSetBundleStatePacket::sound, ClientboundSetBundleStatePacket::new);
      TYPE = new class_9154(CSUtil.rl("set_bundle_state"));
   }
}
