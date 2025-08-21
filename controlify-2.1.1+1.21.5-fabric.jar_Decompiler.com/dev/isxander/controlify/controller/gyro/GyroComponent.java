package dev.isxander.controlify.controller.gyro;

import dev.isxander.controlify.controller.ECSComponent;
import dev.isxander.controlify.controller.impl.ConfigImpl;
import dev.isxander.controlify.controller.serialization.ConfigClass;
import dev.isxander.controlify.controller.serialization.ConfigHolder;
import dev.isxander.controlify.controller.serialization.IConfig;
import dev.isxander.controlify.utils.CUtil;
import net.minecraft.class_2960;

public class GyroComponent implements ECSComponent, ConfigHolder<GyroComponent.Config> {
   public static final class_2960 ID = CUtil.rl("gyro");
   private GyroStateC gyroState;
   private final IConfig<GyroComponent.Config> config;

   public GyroComponent() {
      this.gyroState = GyroStateC.ZERO;
      this.config = new ConfigImpl(GyroComponent.Config::new, GyroComponent.Config.class);
   }

   public GyroStateC getState() {
      return this.gyroState;
   }

   public void setState(GyroStateC state) {
      this.gyroState = state;
   }

   public IConfig<GyroComponent.Config> config() {
      return this.config;
   }

   public class_2960 id() {
      return ID;
   }

   public static class Config implements ConfigClass {
      public boolean calibrated = false;
      public boolean delayedCalibration = false;
      public float lookSensitivity = 0.0F;
      public boolean relativeGyroMode = false;
      public GyroButtonMode requiresButton;
      public GyroYawMode yawMode;
      public boolean flickStick;
      public boolean invertX;
      public boolean invertY;
      public GyroState calibration;

      public Config() {
         this.requiresButton = GyroButtonMode.ON;
         this.yawMode = GyroYawMode.YAW;
         this.flickStick = false;
         this.invertX = false;
         this.invertY = false;
         this.calibration = new GyroState();
      }
   }
}
