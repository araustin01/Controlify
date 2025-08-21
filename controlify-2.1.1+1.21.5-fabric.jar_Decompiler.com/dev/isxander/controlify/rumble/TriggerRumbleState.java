package dev.isxander.controlify.rumble;

public record TriggerRumbleState(float left, float right) {
   public static final TriggerRumbleState NONE = new TriggerRumbleState(0.0F, 0.0F);

   public TriggerRumbleState(float left, float right) {
      this.left = left;
      this.right = right;
   }

   public boolean isZero() {
      return this.left == 0.0F && this.right == 0.0F;
   }

   public TriggerRumbleState mul(float multiplier) {
      return new TriggerRumbleState(this.left * multiplier, this.right * multiplier);
   }

   public float left() {
      return this.left;
   }

   public float right() {
      return this.right;
   }
}
