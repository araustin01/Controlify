package dev.isxander.controlify.rumble;

import com.mojang.serialization.Codec;

public record RumbleState(float strong, float weak) {
   public static final Codec<RumbleState> CODEC;
   public static final RumbleState NONE;

   public RumbleState(float strong, float weak) {
      this.strong = strong;
      this.weak = weak;
   }

   public boolean isZero() {
      return this.strong == 0.0F && this.weak == 0.0F;
   }

   public RumbleState mul(float multiplier) {
      return new RumbleState(this.strong * multiplier, this.weak * multiplier);
   }

   public static RumbleState unpackFromInt(int packed) {
      float strong = (float)((short)(packed >> 16)) / 32767.0F;
      float weak = (float)((short)packed) / 32767.0F;
      return new RumbleState(strong, weak);
   }

   public static int packToInt(RumbleState state) {
      int high = (int)(state.strong() * 32767.0F);
      int low = (int)(state.weak() * 32767.0F);
      return high << 16 | low & '\uffff';
   }

   public float strong() {
      return this.strong;
   }

   public float weak() {
      return this.weak;
   }

   static {
      CODEC = Codec.INT.xmap(RumbleState::unpackFromInt, RumbleState::packToInt);
      NONE = new RumbleState(0.0F, 0.0F);
   }
}
