package dev.isxander.controlify.controller.gyro;

import org.joml.Vector3fc;

public interface GyroStateC extends Vector3fc {
   GyroStateC ZERO = new GyroState(0.0F, 0.0F, 0.0F);

   default float pitch() {
      return this.x();
   }

   default float yaw() {
      return this.y();
   }

   default float roll() {
      return this.z();
   }
}
