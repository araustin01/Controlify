package dev.isxander.controlify.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.isxander.controlify.controller.battery.BatteryLevelComponent;
import dev.isxander.controlify.controller.dualsense.DualSenseComponent;
import dev.isxander.controlify.controller.gyro.GyroComponent;
import dev.isxander.controlify.controller.haptic.HDHapticComponent;
import dev.isxander.controlify.controller.haptic.SimpleHapticComponent;
import dev.isxander.controlify.controller.impl.ConfigImpl;
import dev.isxander.controlify.controller.impl.ECSEntityImpl;
import dev.isxander.controlify.controller.info.ControllerInfo;
import dev.isxander.controlify.controller.info.DriverNameComponent;
import dev.isxander.controlify.controller.info.GUIDComponent;
import dev.isxander.controlify.controller.info.UIDComponent;
import dev.isxander.controlify.controller.input.InputComponent;
import dev.isxander.controlify.controller.keyboard.NativeKeyboardComponent;
import dev.isxander.controlify.controller.misc.BluetoothDeviceComponent;
import dev.isxander.controlify.controller.rumble.RumbleComponent;
import dev.isxander.controlify.controller.rumble.TriggerRumbleComponent;
import dev.isxander.controlify.controller.serialization.ConfigHolder;
import dev.isxander.controlify.controller.serialization.IConfig;
import dev.isxander.controlify.controller.touchpad.TouchpadComponent;
import dev.isxander.controlify.driver.Driver;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.controlify.utils.log.ControlifyLogger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.class_2960;
import org.apache.commons.lang3.SerializationException;
import org.jetbrains.annotations.NotNull;

public class ControllerEntity extends ECSEntityImpl {
   private final ControllerInfo info;
   private final Driver driver;
   private final ControlifyLogger logger;

   public ControllerEntity(ControllerInfo info, Driver driver, ControlifyLogger logger) {
      this.info = info;
      this.driver = driver;
      this.logger = logger;
      this.setComponent(new ConfigImpl(GenericControllerConfig::new, GenericControllerConfig.class));
      driver.addComponents(this);
      this.getAllComponents().values().forEach(ECSComponent::finalise);
      logger.debugLog("Components: {}", this.getAllComponents().keySet().stream().map(class_2960::toString).collect(Collectors.joining(", ")));
   }

   public ControllerUID uid() {
      return (ControllerUID)((UIDComponent)this.getComponent(UIDComponent.ID).orElseThrow()).value();
   }

   @NotNull
   public String driverName() {
      return (String)((DriverNameComponent)this.getComponent(DriverNameComponent.ID).orElseThrow()).value();
   }

   @NotNull
   public String guid() {
      return (String)((GUIDComponent)this.getComponent(GUIDComponent.ID).orElseThrow()).value();
   }

   public ControllerInfo info() {
      return this.info;
   }

   @NotNull
   public String name() {
      String nickname = ((GenericControllerConfig)this.genericConfig().config()).nickname;
      if (nickname != null) {
         return nickname;
      } else {
         String friendlyName = this.info().type().friendlyName();
         return friendlyName != null ? friendlyName : this.driverName();
      }
   }

   public Driver drivers() {
      return this.driver;
   }

   public Optional<InputComponent> input() {
      return this.getComponent(InputComponent.ID);
   }

   public Optional<RumbleComponent> rumble() {
      return this.getComponent(RumbleComponent.ID);
   }

   public Optional<TriggerRumbleComponent> triggerRumble() {
      return this.getComponent(TriggerRumbleComponent.ID);
   }

   public Optional<GyroComponent> gyro() {
      return this.getComponent(GyroComponent.ID);
   }

   public Optional<TouchpadComponent> touchpad() {
      return this.getComponent(TouchpadComponent.ID);
   }

   public Optional<BatteryLevelComponent> batteryLevel() {
      return this.getComponent(BatteryLevelComponent.ID);
   }

   public Optional<HDHapticComponent> hdHaptics() {
      return this.getComponent(HDHapticComponent.ID);
   }

   public Optional<SimpleHapticComponent> simpleHaptics() {
      return this.getComponent(SimpleHapticComponent.ID);
   }

   public Optional<DualSenseComponent> dualSense() {
      return this.getComponent(DualSenseComponent.ID);
   }

   public IConfig<GenericControllerConfig> genericConfig() {
      return (IConfig)this.getComponent(GenericControllerConfig.ID).orElseThrow();
   }

   public Optional<IConfig<GamepadControllerConfig>> gamepadConfig() {
      return this.getComponent(GamepadControllerConfig.ID);
   }

   public Optional<IConfig<JoystickControllerConfig>> joystickConfig() {
      return this.getComponent(JoystickControllerConfig.ID);
   }

   public Optional<BluetoothDeviceComponent> bluetooth() {
      return this.getComponent(BluetoothDeviceComponent.ID);
   }

   public Optional<NativeKeyboardComponent> nativeKeyboard() {
      return this.getComponent(NativeKeyboardComponent.ID);
   }

   public void update(boolean outOfFocus) {
      this.driver.update(this, outOfFocus);
   }

   public Map<class_2960, IConfig<?>> getAllConfigs() {
      Map<class_2960, IConfig<?>> configs = new HashMap();
      this.getAllComponents().forEach((id, component) -> {
         if (component instanceof IConfig) {
            IConfig<?> config = (IConfig)component;
            configs.put(id, config);
         }

         if (component instanceof ConfigHolder) {
            ConfigHolder<?> configHolder = (ConfigHolder)component;
            configs.put(id, configHolder.config());
         }

      });
      return configs;
   }

   public void serializeToObject(JsonObject object, Gson gson) throws SerializationException {
      Iterator var3 = this.getAllConfigs().entrySet().iterator();

      while(var3.hasNext()) {
         Entry<class_2960, IConfig<?>> entry = (Entry)var3.next();
         class_2960 key = (class_2960)entry.getKey();
         IConfig<?> config = (IConfig)entry.getValue();
         object.add(key.toString(), config.serialize(gson, this));
      }

   }

   public void deserializeFromObject(JsonObject object, Gson gson) throws SerializationException {
      Iterator var3 = this.getAllConfigs().entrySet().iterator();

      while(var3.hasNext()) {
         Entry<class_2960, IConfig<?>> entry = (Entry)var3.next();
         class_2960 key = (class_2960)entry.getKey();
         IConfig<?> config = (IConfig)entry.getValue();
         JsonElement element = object.remove(key.toString());
         if (element != null) {
            config.deserialize(element, gson, this);
         } else {
            CUtil.LOGGER.warn("Could not find component config {} whilst deserializing. Ignoring.", key);
         }
      }

   }

   public void resetToDefaultConfig() {
      Iterator var1 = this.getAllConfigs().values().iterator();

      while(var1.hasNext()) {
         IConfig<?> config = (IConfig)var1.next();
         config.resetToDefault();
      }

   }

   public void close() {
      this.driver.close();
   }

   public ControlifyLogger getLogger() {
      return this.logger;
   }
}
