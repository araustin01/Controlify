package dev.isxander.controlify.controller.gyro;

public enum GyroYawMode {
   YAW,
   ROLL,
   BOTH;

   // $FF: synthetic method
   private static GyroYawMode[] $values() {
      return new GyroYawMode[]{YAW, ROLL, BOTH};
   }
}
