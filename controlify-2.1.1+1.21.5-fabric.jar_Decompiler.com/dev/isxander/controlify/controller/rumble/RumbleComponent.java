package dev.isxander.controlify.controller.rumble;

import dev.isxander.controlify.controller.ECSComponent;
import dev.isxander.controlify.controller.impl.ConfigImpl;
import dev.isxander.controlify.controller.serialization.ConfigClass;
import dev.isxander.controlify.controller.serialization.ConfigHolder;
import dev.isxander.controlify.controller.serialization.IConfig;
import dev.isxander.controlify.rumble.RumbleManager;
import dev.isxander.controlify.rumble.RumbleSource;
import dev.isxander.controlify.rumble.RumbleState;
import dev.isxander.controlify.utils.CUtil;
import java.util.Map;
import java.util.Optional;
import net.minecraft.class_2960;

public class RumbleComponent implements ECSComponent, ConfigHolder<RumbleComponent.Config> {
   public static final class_2960 ID = CUtil.rl("rumble");
   private RumbleState state = null;
   private final IConfig<RumbleComponent.Config> config = new ConfigImpl(RumbleComponent.Config::new, RumbleComponent.Config.class);
   private final RumbleManager rumbleManager = new RumbleManager(this);

   public void queueRumble(RumbleState state) {
      if (((RumbleComponent.Config)this.confObj()).enabled) {
         this.state = state;
      }

   }

   public Optional<RumbleState> consumeRumble() {
      RumbleState state = this.state;
      this.state = null;
      return Optional.ofNullable(state);
   }

   public RumbleManager rumbleManager() {
      return this.rumbleManager;
   }

   public IConfig<RumbleComponent.Config> config() {
      return this.config;
   }

   public class_2960 id() {
      return ID;
   }

   public static class Config implements ConfigClass {
      public boolean enabled = true;
      public Map<class_2960, Float> vibrationStrengths = RumbleSource.getDefaultMap();

      public RumbleState applyRumbleStrength(RumbleState state, RumbleSource source) {
         float strength = this.getStrength(source);
         if (source != RumbleSource.MASTER) {
            strength *= this.getStrength(RumbleSource.MASTER);
         }

         return state.mul(strength);
      }

      private float getStrength(RumbleSource source) {
         return (Float)this.vibrationStrengths.getOrDefault(source.id(), 1.0F);
      }
   }
}
