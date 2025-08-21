package dev.isxander.controlify.server.packets;

import dev.isxander.controlify.rumble.ContinuousRumbleEffect;
import dev.isxander.controlify.rumble.RumbleEffect;
import dev.isxander.controlify.rumble.RumbleSource;
import dev.isxander.controlify.rumble.RumbleState;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.controlify.utils.Easings;
import java.util.Objects;
import net.minecraft.class_1297;
import net.minecraft.class_2540;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_9139;

public record EntityVibrationPacket(int entityId, float range, int duration, RumbleState state, RumbleSource source) {
   public static final class_2960 CHANNEL = CUtil.rl("vibrate_from_entity");
   public static final class_9139<class_2540, EntityVibrationPacket> CODEC = class_9139.method_56437((buf, packet) -> {
      buf.method_53002(packet.entityId());
      buf.method_52941(packet.range());
      buf.method_53002(packet.duration());
      buf.method_53002(RumbleState.packToInt(packet.state()));
      buf.method_10812(packet.source().id());
   }, (buf) -> {
      return new EntityVibrationPacket(buf.readInt(), buf.readFloat(), buf.readInt(), RumbleState.unpackFromInt(buf.readInt()), RumbleSource.get(buf.method_10810()));
   });

   public EntityVibrationPacket(int entityId, float range, int duration, RumbleState state, RumbleSource source) {
      this.entityId = entityId;
      this.range = range;
      this.duration = duration;
      this.state = state;
      this.source = source;
   }

   public RumbleEffect createEffect() {
      class_1297 entity = class_310.method_1551().field_1687.method_8469(this.entityId);
      ContinuousRumbleEffect.Builder var10000 = ContinuousRumbleEffect.builder().constant(this.state);
      Objects.requireNonNull(entity);
      return var10000.inWorld(entity::method_19538, 0.0F, 1.0F, this.range, Easings.toFloat(Easings::easeInSine)).timeout(this.duration).build();
   }

   public int entityId() {
      return this.entityId;
   }

   public float range() {
      return this.range;
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
