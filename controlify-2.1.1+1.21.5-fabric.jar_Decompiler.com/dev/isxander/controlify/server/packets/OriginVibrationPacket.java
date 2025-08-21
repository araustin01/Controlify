package dev.isxander.controlify.server.packets;

import dev.isxander.controlify.rumble.ContinuousRumbleEffect;
import dev.isxander.controlify.rumble.RumbleEffect;
import dev.isxander.controlify.rumble.RumbleSource;
import dev.isxander.controlify.rumble.RumbleState;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.controlify.utils.Easings;
import net.minecraft.class_243;
import net.minecraft.class_2540;
import net.minecraft.class_2960;
import net.minecraft.class_9139;
import org.joml.Vector3f;

public record OriginVibrationPacket(Vector3f origin, float effectRange, int duration, RumbleState state, RumbleSource source) {
   public static final class_2960 CHANNEL = CUtil.rl("vibrate_from_origin");
   public static final class_9139<class_2540, OriginVibrationPacket> CODEC = class_9139.method_56437((buf, packet) -> {
      buf.method_49068(packet.origin());
      buf.method_52941(packet.effectRange());
      buf.method_10804(packet.duration());
      buf.method_53002(RumbleState.packToInt(packet.state()));
      buf.method_10812(packet.source().id());
   }, (buf) -> {
      return new OriginVibrationPacket(buf.method_49069(), buf.readFloat(), buf.method_10816(), RumbleState.unpackFromInt(buf.readInt()), RumbleSource.get(buf.method_10810()));
   });

   public OriginVibrationPacket(Vector3f origin, float effectRange, int duration, RumbleState state, RumbleSource source) {
      this.origin = origin;
      this.effectRange = effectRange;
      this.duration = duration;
      this.state = state;
      this.source = source;
   }

   public RumbleEffect createEffect() {
      class_243 originVec3 = new class_243(this.origin);
      return ContinuousRumbleEffect.builder().constant(this.state).inWorld(() -> {
         return originVec3;
      }, 0.0F, 1.0F, this.effectRange, Easings.toFloat(Easings::easeInSine)).timeout(this.duration).build();
   }

   public Vector3f origin() {
      return this.origin;
   }

   public float effectRange() {
      return this.effectRange;
   }

   public int duration() {
      return this.duration;
   }

   public RumbleState state() {
      return this.state;
   }

   public RumbleSource source() {
      return this.source;
   }
}
