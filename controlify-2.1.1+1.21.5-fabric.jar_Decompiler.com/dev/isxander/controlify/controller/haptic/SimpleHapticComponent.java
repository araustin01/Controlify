package dev.isxander.controlify.controller.haptic;

import dev.isxander.controlify.controller.ECSComponent;
import dev.isxander.controlify.controller.impl.ConfigImpl;
import dev.isxander.controlify.controller.serialization.ConfigClass;
import dev.isxander.controlify.controller.serialization.ConfigHolder;
import dev.isxander.controlify.controller.serialization.IConfig;
import dev.isxander.controlify.utils.CUtil;
import net.minecraft.class_2960;

public class SimpleHapticComponent implements ECSComponent, ConfigHolder<SimpleHapticComponent.Config> {
   public static final class_2960 ID = CUtil.rl("hd_haptics");
   private final IConfig<SimpleHapticComponent.Config> config = new ConfigImpl(SimpleHapticComponent.Config::new, SimpleHapticComponent.Config.class);
   private Runnable onHaptic;

   public void playHaptic() {
      if (((SimpleHapticComponent.Config)this.confObj()).enabled) {
         this.onHaptic.run();
      }

   }

   public void applyOnHaptic(Runnable onHaptic) {
      this.onHaptic = onHaptic;
   }

   public IConfig<SimpleHapticComponent.Config> config() {
      return this.config;
   }

   public class_2960 id() {
      return ID;
   }

   public static class Config implements ConfigClass {
      public boolean enabled;
   }
}
