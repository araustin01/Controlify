package dev.isxander.controlify.server.packets;

import dev.isxander.controlify.rumble.BasicRumbleEffect;
import dev.isxander.controlify.rumble.RumbleEffect;
import dev.isxander.controlify.rumble.RumbleSource;
import dev.isxander.controlify.rumble.RumbleState;
import dev.isxander.controlify.utils.CUtil;
import net.minecraft.class_2540;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_9139;

public record VibrationPacket(RumbleSource source, RumbleState[] frames) {
   public static final class_2960 CHANNEL = CUtil.rl("vibration");
   public static final class_9139<class_2540, VibrationPacket> CODEC = class_9139.method_56437((buf, packet) -> {
      buf.method_10812(packet.source().id());
      buf.method_53002(packet.frames().length);
      RumbleState[] var2 = packet.frames();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         RumbleState frame = var2[var4];
         buf.method_53002(RumbleState.packToInt(frame));
      }

   }, (buf) -> {
      RumbleSource source = RumbleSource.get(buf.method_10810());
      RumbleState[] frames = new RumbleState[buf.readInt()];

      for(int i = 0; i < frames.length; ++i) {
         frames[i] = RumbleState.unpackFromInt(buf.readInt());
      }

      return new VibrationPacket(source, frames);
   });

   public VibrationPacket(RumbleSource source, RumbleState[] frames) {
      this.source = source;
      this.frames = frames;
   }

   public RumbleEffect createEffect() {
      return (new BasicRumbleEffect(this.frames)).earlyFinish(() -> {
         return class_310.method_1551().field_1687 == null;
      });
   }

   public RumbleSource source() {
      return this.source;
   }

   public RumbleState[] frames() {
      return this.frames;
   }
}
