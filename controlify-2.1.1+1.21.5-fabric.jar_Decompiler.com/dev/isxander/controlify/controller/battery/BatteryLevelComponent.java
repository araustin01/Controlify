package dev.isxander.controlify.controller.battery;

import dev.isxander.controlify.controller.ECSComponent;
import dev.isxander.controlify.utils.CUtil;
import net.minecraft.class_2960;

public class BatteryLevelComponent implements ECSComponent {
   public static final class_2960 ID = CUtil.rl("battery_level");
   private PowerState batteryLevel = new PowerState.Unknown();

   public PowerState getBatteryLevel() {
      return this.batteryLevel;
   }

   public void setBatteryLevel(PowerState batteryLevel) {
      this.batteryLevel = batteryLevel;
   }

   public class_2960 id() {
      return ID;
   }
}
