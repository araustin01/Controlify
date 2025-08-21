package dev.isxander.controlify.controller.misc;

import dev.isxander.controlify.controller.ECSComponent;
import dev.isxander.controlify.controller.impl.ConfigImpl;
import dev.isxander.controlify.controller.serialization.ConfigClass;
import dev.isxander.controlify.controller.serialization.ConfigHolder;
import dev.isxander.controlify.controller.serialization.IConfig;
import dev.isxander.controlify.utils.CUtil;
import net.minecraft.class_2960;

public class BluetoothDeviceComponent implements ECSComponent, ConfigHolder<BluetoothDeviceComponent.Config> {
   public static final class_2960 ID = CUtil.rl("bluetooth");
   private final IConfig<BluetoothDeviceComponent.Config> config = new ConfigImpl(BluetoothDeviceComponent.Config::new, BluetoothDeviceComponent.Config.class);

   public IConfig<BluetoothDeviceComponent.Config> config() {
      return this.config;
   }

   public class_2960 id() {
      return ID;
   }

   public static class Config implements ConfigClass {
      public boolean dontShowWarningAgain = false;
   }
}
