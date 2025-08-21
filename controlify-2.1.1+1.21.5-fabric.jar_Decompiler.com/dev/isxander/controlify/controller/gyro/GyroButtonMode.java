package dev.isxander.controlify.controller.gyro;

public enum GyroButtonMode {
   ON,
   INVERT,
   TOGGLE,
   OFF;

   // $FF: synthetic method
   private static GyroButtonMode[] $values() {
      return new GyroButtonMode[]{ON, INVERT, TOGGLE, OFF};
   }
}
