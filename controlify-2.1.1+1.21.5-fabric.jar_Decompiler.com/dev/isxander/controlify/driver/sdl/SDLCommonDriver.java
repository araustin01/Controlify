package dev.isxander.controlify.driver.sdl;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.ControllerUID;
import dev.isxander.controlify.controller.battery.BatteryLevelComponent;
import dev.isxander.controlify.controller.battery.PowerState;
import dev.isxander.controlify.controller.dualsense.DualSenseComponent;
import dev.isxander.controlify.controller.haptic.CompleteSoundData;
import dev.isxander.controlify.controller.haptic.HDHapticComponent;
import dev.isxander.controlify.controller.id.ControllerType;
import dev.isxander.controlify.controller.info.DriverNameComponent;
import dev.isxander.controlify.controller.info.GUIDComponent;
import dev.isxander.controlify.controller.info.UIDComponent;
import dev.isxander.controlify.controller.misc.BluetoothDeviceComponent;
import dev.isxander.controlify.controller.rumble.RumbleComponent;
import dev.isxander.controlify.controller.rumble.TriggerRumbleComponent;
import dev.isxander.controlify.controllermanager.ControllerManager;
import dev.isxander.controlify.driver.Driver;
import dev.isxander.controlify.driver.sdl.dualsense.DS5EffectsState;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.controlify.utils.log.ControlifyLogger;
import dev.isxander.sdl3java.api.audio.SDL_AudioDeviceID;
import dev.isxander.sdl3java.api.audio.SDL_AudioFormat;
import dev.isxander.sdl3java.api.audio.SDL_AudioSpec;
import dev.isxander.sdl3java.api.audio.SDL_AudioStream;
import dev.isxander.sdl3java.api.audio.SdlAudio;
import dev.isxander.sdl3java.api.audio.SDL_AudioSpec.ByReference;
import dev.isxander.sdl3java.api.error.SdlError;
import dev.isxander.sdl3java.api.guid.SdlGuid;
import dev.isxander.sdl3java.api.joystick.SDL_JoystickGUID;
import dev.isxander.sdl3java.api.joystick.SDL_JoystickID;
import dev.isxander.sdl3java.api.properties.SDL_PropertiesID;
import dev.isxander.sdl3java.api.properties.SdlProperties;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javax.sound.sampled.AudioFormat.Encoding;
import net.minecraft.class_156;
import net.minecraft.class_156.class_158;
import org.jetbrains.annotations.Nullable;

public abstract class SDLCommonDriver<SDL_Controller> implements Driver {
   private static final int AUDIO_STREAM_TIMEOUT_TICKS = 360000;
   private final ControlifyLogger logger;
   protected SDL_Controller ptrController;
   protected BatteryLevelComponent batteryLevelComponent;
   protected RumbleComponent rumbleComponent;
   protected TriggerRumbleComponent triggerRumbleComponent;
   protected HDHapticComponent hdHapticComponent;
   protected DualSenseComponent dualSenseComponent;
   protected final boolean isRumbleSupported;
   protected final boolean isTriggerRumbleSupported;
   protected final boolean isDualsense;
   protected final SDL_JoystickGUID guid;
   protected final String guidString;
   @Nullable
   protected final String serial;
   protected final String name;
   protected final SDL_PropertiesID props;
   protected final short vendorId;
   protected final short productId;
   protected final SDLJoystickConnectionState connectionState;
   @Nullable
   protected SDL_AudioDeviceID dualsenseAudioDev;
   @Nullable
   protected SDL_AudioSpec dualsenseAudioSpec;
   protected final List<SDLCommonDriver.AudioStreamHandle> dualsenseAudioHandles;

   public SDLCommonDriver(SDL_Controller ptrController, SDL_JoystickID jid, ControllerType type, ControlifyLogger logger) {
      this.ptrController = ptrController;
      this.logger = logger;
      this.props = this.SDL_GetControllerProperties(ptrController);
      this.name = this.SDL_GetControllerName(ptrController);
      this.guid = this.SDL_GetControllerGUIDForID(jid);
      this.guidString = SdlGuid.SDL_GUIDToString(this.guid);
      logger.debugLog("SDL GUID: {}", this.guidString);
      this.serial = this.SDL_GetControllerSerial(ptrController);
      logger.debugLog("SDL Serial: {}", this.serial);
      this.vendorId = this.SDL_GetControllerVendor(ptrController);
      this.productId = this.SDL_GetControllerProduct(ptrController);
      logger.debugLog("SDL VID: {} PID: {}", this.vendorId, this.productId);
      this.connectionState = SDLJoystickConnectionState.fromInt(this.SDL_GetControllerConnectionState(ptrController));
      logger.debugLog("SDL Connection State: {}", this.connectionState);
      this.isRumbleSupported = SdlProperties.SDL_GetBooleanProperty(this.props, "SDL.joystick.cap.rumble", false);
      this.isTriggerRumbleSupported = SdlProperties.SDL_GetBooleanProperty(this.props, "SDL.joystick.cap.trigger_rumble", false);
      DecodedGUID decodedGuid = DecodedGUID.fromGUID(this.guid);
      logger.log("SDL GUID driver signature: {}", decodedGuid.getDriverHint());
      this.dualsenseAudioHandles = new ArrayList();
      if (CUtil.rl("dualsense").equals(type.namespace())) {
         this.isDualsense = true;
         logger.debugLog("DualSense controller detected.");
         if (class_156.method_668() != class_158.field_1137) {
            SDL_AudioDeviceID dualsenseAudioDev = null;
            ByReference devSpec = new ByReference();
            SDL_AudioDeviceID[] var8 = SdlAudio.SDL_GetAudioPlaybackDevices();
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               SDL_AudioDeviceID dev = var8[var10];
               String name = SdlAudio.SDL_GetAudioDeviceName(dev).toLowerCase();
               if (name.contains("dualsense") || name.contains("ps5") || name.contains("wireless controller")) {
                  SdlAudio.SDL_GetAudioDeviceFormat(dev, devSpec, (IntByReference)null);
                  if (devSpec.channels == 4) {
                     dualsenseAudioDev = dev;
                     break;
                  }
               }
            }

            if (dualsenseAudioDev != null) {
               logger.debugLog("DualSense HD Haptics audio device found.");
               this.dualsenseAudioSpec = devSpec;
               this.dualsenseAudioDev = SdlAudio.SDL_OpenAudioDevice(dualsenseAudioDev, (ByReference)this.dualsenseAudioSpec);
            } else {
               logger.debugLog("DualSense HD Haptics audio device not found.");
            }
         }
      } else {
         this.isDualsense = false;
      }

   }

   public void addComponents(ControllerEntity controller) {
      controller.setComponent(new DriverNameComponent(this.name));
      controller.setComponent(new GUIDComponent(this.guidString));
      controller.setComponent(new UIDComponent(this.createUid()));
      controller.setComponent(this.batteryLevelComponent = new BatteryLevelComponent());
      if (this.isRumbleSupported) {
         controller.setComponent(this.rumbleComponent = new RumbleComponent());
      }

      if (this.isTriggerRumbleSupported) {
         controller.setComponent(this.triggerRumbleComponent = new TriggerRumbleComponent());
      }

      if (this.isDualsense) {
         controller.setComponent(this.dualSenseComponent = new DualSenseComponent());
      }

      if (this.dualsenseAudioDev != null) {
         controller.setComponent(this.hdHapticComponent = new HDHapticComponent());
         this.hdHapticComponent.acceptPlayHaptic(this::playHaptic);
      }

      if (this.isBluetooth()) {
         controller.setComponent(new BluetoothDeviceComponent());
      }

   }

   public void update(ControllerEntity controller, boolean outOfFocus) {
      if (this.ptrController == null) {
         throw new IllegalStateException("Tried to update controller when it's closed.");
      } else {
         this.updateRumble();
         this.updateBatteryLevel();
         this.updateDualSense();
         this.updateHDHaptic();
      }
   }

   public void close() {
      if (this.ptrController == null) {
         throw new IllegalStateException("Tried to close controller when it's already closed.");
      } else {
         this.SDL_CloseController(this.ptrController);
         this.ptrController = null;
         if (this.dualsenseAudioDev != null) {
            SdlAudio.SDL_CloseAudioDevice(this.dualsenseAudioDev);
            this.dualsenseAudioDev = null;
            Iterator var1 = this.dualsenseAudioHandles.iterator();

            while(var1.hasNext()) {
               SDLCommonDriver.AudioStreamHandle handle = (SDLCommonDriver.AudioStreamHandle)var1.next();
               handle.close();
            }
         }

      }
   }

   protected void updateRumble() {
      Optional stateOpt;
      if (this.isRumbleSupported) {
         stateOpt = this.rumbleComponent.consumeRumble();
         stateOpt.ifPresent((state) -> {
            if (!this.SDL_RumbleController(this.ptrController, state.strong(), state.weak(), 5000)) {
               CUtil.LOGGER.error("Could not rumble gamepad: {}", SdlError.SDL_GetError());
            }

         });
      }

      if (this.isTriggerRumbleSupported) {
         stateOpt = this.triggerRumbleComponent.consumeTriggerRumble();
         stateOpt.ifPresent((state) -> {
            if (!this.SDL_RumbleControllerTriggers(this.ptrController, state.left(), state.right(), 0)) {
               CUtil.LOGGER.error("Could not rumble triggers gamepad: {}", SdlError.SDL_GetError());
            }

         });
      }

   }

   private void updateBatteryLevel() {
      IntByReference percent = new IntByReference();
      int powerState = this.SDL_GetControllerPowerInfo(this.ptrController, percent);
      Object var10000;
      switch(powerState) {
      case -1:
      case 0:
         var10000 = new PowerState.Unknown();
         break;
      case 1:
         var10000 = new PowerState.Depleting(percent.getValue());
         break;
      case 2:
         var10000 = new PowerState.WiredOnly();
         break;
      case 3:
         var10000 = new PowerState.Charging(percent.getValue());
         break;
      case 4:
         var10000 = new PowerState.Full();
         break;
      default:
         throw new IllegalStateException("Unexpected value");
      }

      PowerState level = var10000;
      this.batteryLevelComponent.setBatteryLevel((PowerState)level);
   }

   private void updateDualSense() {
      if (this.dualSenseComponent != null) {
         DS5EffectsState.ByValue effectsState = new DS5EffectsState.ByValue();
         if (this.dualSenseComponent.consumeDirty()) {
            effectsState.ucEnableBits1 = (byte)(effectsState.ucEnableBits1 | 8);
            effectsState.rgucLeftTriggerEffect = this.dualSenseComponent.getLeftTriggerEffect();
            effectsState.ucEnableBits1 = (byte)(effectsState.ucEnableBits1 | 4);
            effectsState.rgucRightTriggerEffect = this.dualSenseComponent.getRightTriggerEffect();
            effectsState.ucEnableBits2 = (byte)(effectsState.ucEnableBits2 | 1);
            effectsState.ucMicLightMode = DS5EffectsState.MuteLightState.fromBoolean(this.dualSenseComponent.getMuteLight());
            effectsState.write();
            this.SDL_SendControllerEffect(this.ptrController, effectsState.getPointer(), Native.getNativeSize(DS5EffectsState.ByValue.class));
         }

      }
   }

   private void updateHDHaptic() {
      for(int i = 0; i < this.dualsenseAudioHandles.size(); ++i) {
         SDLCommonDriver.AudioStreamHandle handle = (SDLCommonDriver.AudioStreamHandle)this.dualsenseAudioHandles.get(i);
         if (handle.isTimedOut()) {
            handle.close();
            this.dualsenseAudioHandles.remove(handle);
         } else {
            handle.tick();
         }
      }

   }

   private void playHaptic(CompleteSoundData sound) {
      if (this.ptrController != null && this.dualsenseAudioDev != null && this.dualsenseAudioSpec != null) {
         SDL_AudioSpec spec = new SDL_AudioSpec();
         spec.channels = sound.format().getChannels();
         spec.freq = (int)sound.format().getSampleRate();
         int ss = sound.format().getSampleSizeInBits();
         int byteSs = ss / 8;
         Encoding encoding = sound.format().getEncoding();
         if (ss == 8) {
            if (encoding == Encoding.PCM_SIGNED) {
               spec.format = new SDL_AudioFormat(32776L);
            } else if (encoding == Encoding.PCM_UNSIGNED) {
               spec.format = new SDL_AudioFormat(8L);
            }
         } else if (sound.format().isBigEndian()) {
            audioFmtEndian(spec, ss, encoding, 36880, 36896, 37152);
         } else {
            audioFmtEndian(spec, ss, encoding, 32784, 32800, 33056);
         }

         if (spec.format == null) {
            throw new IllegalStateException("Unsupported format");
         } else {
            SDLCommonDriver.AudioStreamHandle handle = null;
            Iterator var7 = this.dualsenseAudioHandles.iterator();

            SDLCommonDriver.AudioStreamHandle newHandle;
            while(var7.hasNext()) {
               newHandle = (SDLCommonDriver.AudioStreamHandle)var7.next();
               SDL_AudioSpec streamSpec = newHandle.getSpec();
               if (streamSpec.format.intValue() == spec.format.intValue() && streamSpec.freq == spec.freq && streamSpec.channels == spec.channels && !newHandle.isInUse()) {
                  handle = newHandle;
                  break;
               }
            }

            int length = sound.audio().length / spec.freq / spec.channels / byteSs * 20;
            if (handle != null) {
               handle.queueAudio(sound.audio(), length);
            } else {
               if (this.dualsenseAudioHandles.size() >= 16) {
                  ((SDLCommonDriver.AudioStreamHandle)this.dualsenseAudioHandles.remove(0)).close();
               }

               newHandle = SDLCommonDriver.AudioStreamHandle.createWithAudio(this.dualsenseAudioDev, spec, this.dualsenseAudioSpec, sound.audio(), length);
               this.dualsenseAudioHandles.add(newHandle);
            }

         }
      }
   }

   protected ControllerUID createUid() {
      int identifiers = 0;
      List<byte[]> bytes = new ArrayList();
      if (this.vendorId != 0 && this.productId != 0) {
         bytes.add(new byte[]{(byte)(this.vendorId >> 8), (byte)this.vendorId, (byte)(this.productId >> 8), (byte)this.productId});
         ++identifiers;
      }

      if (this.serial != null) {
         bytes.add(this.serial.getBytes());
         ++identifiers;
      }

      if (identifiers == 0) {
         bytes.add((byte[])this.guid.data.clone());
      }

      String uid = CUtil.createUIDFromBytes((byte[][])bytes.toArray(new byte[0][]));
      int duplicateCount = (int)((ControllerManager)Controlify.instance().getControllerManager().orElseThrow()).getConnectedControllers().stream().filter((controller) -> {
         return controller.uid().string().startsWith(uid);
      }).count();
      if (duplicateCount > 0) {
         uid = uid + "-" + duplicateCount;
      }

      return new ControllerUID(uid);
   }

   protected boolean isBluetooth() {
      return this.connectionState == SDLJoystickConnectionState.WIRELESS;
   }

   protected abstract SDL_PropertiesID SDL_GetControllerProperties(SDL_Controller var1);

   protected abstract String SDL_GetControllerName(SDL_Controller var1);

   protected abstract SDL_JoystickGUID SDL_GetControllerGUIDForID(SDL_JoystickID var1);

   @Nullable
   protected abstract String SDL_GetControllerSerial(SDL_Controller var1);

   protected abstract short SDL_GetControllerVendor(SDL_Controller var1);

   protected abstract short SDL_GetControllerProduct(SDL_Controller var1);

   protected abstract int SDL_GetControllerConnectionState(SDL_Controller var1);

   protected abstract boolean SDL_CloseController(SDL_Controller var1);

   protected abstract boolean SDL_RumbleController(SDL_Controller var1, float var2, float var3, int var4);

   protected abstract boolean SDL_RumbleControllerTriggers(SDL_Controller var1, float var2, float var3, int var4);

   protected abstract int SDL_GetControllerPowerInfo(SDL_Controller var1, IntByReference var2);

   protected abstract boolean SDL_SendControllerEffect(SDL_Controller var1, Pointer var2, int var3);

   private static void audioFmtEndian(SDL_AudioSpec spec, int ss, Encoding encoding, int signed16, int signed32, int float32) {
      if (ss == 16) {
         if (encoding == Encoding.PCM_SIGNED) {
            spec.format = new SDL_AudioFormat((long)signed16);
         }
      } else if (ss == 32) {
         if (encoding == Encoding.PCM_SIGNED) {
            spec.format = new SDL_AudioFormat((long)signed32);
         } else if (encoding == Encoding.PCM_FLOAT) {
            spec.format = new SDL_AudioFormat((long)float32);
         }
      }

   }

   protected static class AudioStreamHandle {
      private int streamLastPlayed;
      private final SDL_AudioStream stream;
      private final SDL_AudioSpec spec;

      private AudioStreamHandle(SDL_AudioStream stream, SDL_AudioSpec spec) {
         this.stream = stream;
         this.spec = spec;
         this.streamLastPlayed = 0;
      }

      public void queueAudio(byte[] audio, int tickLength) {
         Memory memory = new Memory((long)audio.length);

         try {
            memory.write(0L, audio, 0, audio.length);
            SdlAudio.SDL_PutAudioStreamData(this.stream, memory, audio.length);
            this.streamLastPlayed = Math.min(0, this.streamLastPlayed);
            this.streamLastPlayed -= tickLength;
         } catch (Throwable var7) {
            try {
               memory.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }

            throw var7;
         }

         memory.close();
      }

      public SDL_AudioSpec getSpec() {
         return this.spec;
      }

      public boolean isInUse() {
         return this.streamLastPlayed < 0;
      }

      public boolean isTimedOut() {
         return this.streamLastPlayed >= 360000;
      }

      public void tick() {
         ++this.streamLastPlayed;
      }

      public void close() {
         SdlAudio.SDL_DestroyAudioStream(this.stream);
      }

      public static SDLCommonDriver.AudioStreamHandle createWithAudio(SDL_AudioDeviceID device, SDL_AudioSpec audioSpec, SDL_AudioSpec devSpec, byte[] audio, int tickLength) {
         SDL_AudioStream stream = SdlAudio.SDL_CreateAudioStream(audioSpec, devSpec);
         SdlAudio.SDL_BindAudioStream(device, stream);
         int[] var10000;
         switch(audioSpec.channels) {
         case 1:
            var10000 = new int[]{-1, -1, 0, 0};
            break;
         case 2:
            var10000 = new int[]{-1, -1, 0, 1};
            break;
         default:
            throw new IllegalStateException("Unsupported channel count " + audioSpec.channels);
         }

         int[] channelMap = var10000;
         if (!SdlAudio.SDL_SetAudioStreamOutputChannelMap(stream, channelMap)) {
            System.out.println(SdlError.SDL_GetError());
         }

         SDLCommonDriver.AudioStreamHandle handle = new SDLCommonDriver.AudioStreamHandle(stream, audioSpec);
         handle.queueAudio(audio, tickLength);
         return handle;
      }
   }
}
