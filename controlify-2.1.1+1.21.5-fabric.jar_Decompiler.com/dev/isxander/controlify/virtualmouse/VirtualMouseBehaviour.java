package dev.isxander.controlify.virtualmouse;

public enum VirtualMouseBehaviour {
   DEFAULT,
   ENABLED,
   DISABLED,
   CURSOR_ONLY;

   public boolean hasCursor() {
      return this != DISABLED;
   }

   public boolean isDefaultOr(VirtualMouseBehaviour behaviour) {
      return this == DEFAULT || this == behaviour;
   }

   // $FF: synthetic method
   private static VirtualMouseBehaviour[] $values() {
      return new VirtualMouseBehaviour[]{DEFAULT, ENABLED, DISABLED, CURSOR_ONLY};
   }
}
