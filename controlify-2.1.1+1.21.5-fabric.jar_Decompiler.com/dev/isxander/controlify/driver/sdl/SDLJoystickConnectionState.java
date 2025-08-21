package dev.isxander.controlify.driver.sdl;

public enum SDLJoystickConnectionState {
   INVALID(-1),
   UNKNOWN(0),
   WIRED(1),
   WIRELESS(2);

   private final int asInt;

   private SDLJoystickConnectionState(int asInt) {
      this.asInt = asInt;
   }

   public int asInt() {
      return this.asInt;
   }

   public static SDLJoystickConnectionState fromInt(int asInt) {
      SDLJoystickConnectionState[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         SDLJoystickConnectionState state = var1[var3];
         if (state.asInt() == asInt) {
            return state;
         }
      }

      throw new IllegalArgumentException("Invalid connection state: " + asInt);
   }

   // $FF: synthetic method
   private static SDLJoystickConnectionState[] $values() {
      return new SDLJoystickConnectionState[]{INVALID, UNKNOWN, WIRED, WIRELESS};
   }
}
