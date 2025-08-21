package dev.isxander.controlify.controllermanager;

import com.google.common.io.ByteStreams;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.id.ControllerType;
import dev.isxander.controlify.controller.info.ControllerInfo;
import dev.isxander.controlify.debug.DebugProperties;
import dev.isxander.controlify.driver.CompoundDriver;
import dev.isxander.controlify.driver.Driver;
import dev.isxander.controlify.driver.sdl.SDL3GamepadDriver;
import dev.isxander.controlify.driver.sdl.SDL3JoystickDriver;
import dev.isxander.controlify.driver.sdl.SDL3NativesManager;
import dev.isxander.controlify.driver.sdl.SDLUtil;
import dev.isxander.controlify.driver.steamdeck.SteamDeckDriver;
import dev.isxander.controlify.driver.steamdeck.SteamDeckUtil;
import dev.isxander.controlify.hid.ControllerHIDService;
import dev.isxander.controlify.hid.HIDDevice;
import dev.isxander.controlify.hid.HIDIdentifier;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.controlify.utils.ControllerUtils;
import dev.isxander.controlify.utils.log.ControlifyLogger;
import dev.isxander.sdl3java.api.error.SdlError;
import dev.isxander.sdl3java.api.events.SDL_EventFilter;
import dev.isxander.sdl3java.api.events.SdlEvents;
import dev.isxander.sdl3java.api.events.events.SDL_Event;
import dev.isxander.sdl3java.api.gamepad.SDL_Gamepad;
import dev.isxander.sdl3java.api.gamepad.SdlGamepad;
import dev.isxander.sdl3java.api.iostream.SDL_IOStream;
import dev.isxander.sdl3java.api.iostream.SdlIOStream;
import dev.isxander.sdl3java.api.joystick.SDL_Joystick;
import dev.isxander.sdl3java.api.joystick.SDL_JoystickGUID;
import dev.isxander.sdl3java.api.joystick.SDL_JoystickID;
import dev.isxander.sdl3java.api.joystick.SdlJoystick;
import dev.isxander.sdl3java.jna.size_t;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.class_3298;
import net.minecraft.class_5912;
import org.jetbrains.annotations.NotNull;

public class SDLControllerManager extends AbstractControllerManager {
   private SDL_Event event = new SDL_Event();
   private final SDLControllerManager.EventFilter eventFilter;
   private boolean steamDeckConsumed = false;

   public SDLControllerManager(ControlifyLogger logger) {
      super(logger);
      logger.debugLog("Controller manager using SDL3");
      logger.validateIsTrue(SDL3NativesManager.isLoaded(), "SDL3 natives must be loaded before creating SDLControllerManager");
      SdlEvents.SDL_SetEventFilter(this.eventFilter = new SDLControllerManager.EventFilter(), Pointer.NULL);
   }

   public void tick(boolean outOfFocus) {
      super.tick(outOfFocus);
      SdlEvents.SDL_PumpEvents();
      if (this.event == null) {
         this.logger.error("SDL_Event has somehow been set to null. Recreating...");
         this.event = new SDL_Event();
      }

      while(SdlEvents.SDL_PollEvent(this.event)) {
         SDL_JoystickID jid;
         switch(this.event.type) {
         case 1541:
            jid = this.event.jdevice.which;
            this.logger.validateIsTrue(jid != null, "event.jdevice.which was null during SDL_EVENT_JOYSTICK_ADDED event");
            this.logger.debugLog("SDL event: Joystick added: {}", jid.intValue());
            UniqueControllerID ucid = new SDLControllerManager.SDLUniqueControllerID(jid);
            Optional<ControllerEntity> controllerOpt = this.tryCreate(ucid, (ControllerHIDService.ControllerHIDInfo)fetchTypeFromSDL(jid).orElse(new ControllerHIDService.ControllerHIDInfo(ControllerType.DEFAULT, Optional.empty())));
            controllerOpt.ifPresent((controller) -> {
               ControllerUtils.wrapControllerError(() -> {
                  this.onControllerConnected(controller, true);
               }, "Connecting controller", controller);
            });
            break;
         case 1542:
            jid = this.event.jdevice.which;
            this.logger.validateIsTrue(jid != null, "event.jdevice.which was null during SDL_EVENT_JOYSTICK_REMOVED event");
            this.logger.debugLog("SDL event: Joystick removed: {}", jid.intValue());
            this.getController(new SDLControllerManager.SDLUniqueControllerID(jid)).ifPresentOrElse(this::onControllerRemoved, () -> {
               CUtil.LOGGER.warn("Controller removed but not found: {}", jid.intValue());
            });
         }
      }

      SdlGamepad.SDL_UpdateGamepads();
      SdlJoystick.SDL_UpdateJoysticks();
   }

   public void discoverControllers() {
      this.logger.debugLog("Discovering controllers...");
      SDL_JoystickID[] joysticks = SdlJoystick.SDL_GetJoysticks();
      SDL_JoystickID[] var2 = joysticks;
      int var3 = joysticks.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         SDL_JoystickID jid = var2[var4];
         Optional<ControllerEntity> controllerOpt = this.tryCreate(new SDLControllerManager.SDLUniqueControllerID(jid), (ControllerHIDService.ControllerHIDInfo)fetchTypeFromSDL(jid).orElse(new ControllerHIDService.ControllerHIDInfo(ControllerType.DEFAULT, Optional.empty())));
         controllerOpt.ifPresent((controller) -> {
            this.onControllerConnected(controller, false);
         });
      }

   }

   protected Optional<ControllerEntity> createController(UniqueControllerID ucid, ControllerHIDService.ControllerHIDInfo hidInfo, ControlifyLogger controllerLogger) {
      SDL_JoystickID jid = ((SDLControllerManager.SDLUniqueControllerID)ucid).jid();
      controllerLogger.debugLog("Creating controller: {}", jid.intValue());
      boolean isGamepad = this.isControllerGamepad(ucid) && !DebugProperties.FORCE_JOYSTICK;
      controllerLogger.debugLog("Controller is gamepad: {}", isGamepad);
      List<Driver> drivers = new ArrayList();
      if (SteamDeckUtil.DECK_MODE.isGamingMode() && !this.steamDeckConsumed && hidInfo.type().namespace().equals(SteamDeckUtil.STEAM_DECK_NAMESPACE)) {
         controllerLogger.debugLog("Controller is steam deck candidate");
         Optional<SteamDeckDriver> steamDeckDriver = SteamDeckDriver.create(controllerLogger);
         if (steamDeckDriver.isPresent()) {
            drivers.add((Driver)steamDeckDriver.get());
            this.steamDeckConsumed = true;
            controllerLogger.debugLog("Adding SteamDeckDriver - this controller has been reserved for Steam Deck");
         }
      }

      if (isGamepad) {
         SDL_Gamepad ptrGamepad = SDLUtil.openGamepad(jid);
         drivers.add(new SDL3GamepadDriver(ptrGamepad, jid, hidInfo.type(), controllerLogger));
      } else {
         SDL_Joystick ptrJoystick = SDLUtil.openJoystick(jid);
         drivers.add(new SDL3JoystickDriver(ptrJoystick, jid, hidInfo.type(), controllerLogger));
      }

      controllerLogger.debugLog("Drivers: {}", drivers.stream().map((driver) -> {
         return driver.getClass().getSimpleName();
      }).collect(Collectors.joining(", ")));
      CompoundDriver compoundDriver = new CompoundDriver(drivers);
      ControllerInfo info = new ControllerInfo(ucid, hidInfo.type(), hidInfo.hidDevice());
      ControllerEntity controller = new ControllerEntity(info, compoundDriver, controllerLogger);
      controllerLogger.debugLog("Unique Controller ID: {}", info.ucid());
      this.addController(ucid, controller);
      return Optional.of(controller);
   }

   public boolean probeConnectedControllers() {
      return SdlJoystick.SDL_HasJoystick() || SdlGamepad.SDL_HasGamepad();
   }

   public boolean isControllerGamepad(UniqueControllerID ucid) {
      SDL_JoystickID jid = ((SDLControllerManager.SDLUniqueControllerID)ucid).jid;
      return SdlGamepad.SDL_IsGamepad(jid);
   }

   protected String getControllerSystemName(UniqueControllerID ucid) {
      SDL_JoystickID jid = ((SDLControllerManager.SDLUniqueControllerID)ucid).jid;
      return this.isControllerGamepad(ucid) ? SdlGamepad.SDL_GetGamepadNameForID(jid) : SdlJoystick.SDL_GetJoystickNameForID(jid);
   }

   private Optional<ControllerEntity> getController(UniqueControllerID ucid) {
      return Optional.ofNullable((ControllerEntity)this.controllersByJid.getOrDefault(ucid, (Object)null));
   }

   protected void loadGamepadMappings(class_5912 resourceProvider) {
      CUtil.LOGGER.debugLog("Loading gamepad mappings...");
      Optional<class_3298> resourceOpt = resourceProvider.method_14486(CUtil.rl("controllers/gamecontrollerdb-sdl3.txt"));
      if (resourceOpt.isEmpty()) {
         CUtil.LOGGER.error("Failed to find game controller database.");
      } else {
         try {
            InputStream is = ((class_3298)resourceOpt.get()).method_14482();

            try {
               byte[] bytes = ByteStreams.toByteArray(is);
               Memory memory = new Memory((long)bytes.length);

               try {
                  memory.write(0L, bytes, 0, bytes.length);
                  SDL_IOStream stream = SdlIOStream.SDL_IOFromConstMem(memory, new size_t((long)bytes.length));
                  if (stream == null) {
                     throw new IllegalStateException("Failed to open stream");
                  }

                  int count = SdlGamepad.SDL_AddGamepadMappingsFromIO(stream, true);
                  if (count < 0) {
                     CUtil.LOGGER.error("Failed to load gamepad mappings: {}", SdlError.SDL_GetError());
                  } else if (count == 0) {
                     CUtil.LOGGER.warn("Successfully applied gamepad mappings but none were found for this OS. Unsupported OS?");
                  } else {
                     CUtil.LOGGER.log("Successfully loaded {} gamepad mapping entries!", count);
                  }
               } catch (Throwable var10) {
                  try {
                     memory.close();
                  } catch (Throwable var9) {
                     var10.addSuppressed(var9);
                  }

                  throw var10;
               }

               memory.close();
            } catch (Throwable var11) {
               if (is != null) {
                  try {
                     is.close();
                  } catch (Throwable var8) {
                     var11.addSuppressed(var8);
                  }
               }

               throw var11;
            }

            if (is != null) {
               is.close();
            }
         } catch (Throwable var12) {
            CUtil.LOGGER.error("Failed to load gamepad mappings", var12);
         }

      }
   }

   private static Optional<ControllerHIDService.ControllerHIDInfo> fetchTypeFromSDL(SDL_JoystickID jid) {
      int vid = SdlJoystick.SDL_GetJoystickVendorForID(jid);
      int pid = SdlJoystick.SDL_GetJoystickProductForID(jid);
      SDL_JoystickGUID guid = SdlJoystick.SDL_GetJoystickGUIDForID(jid);
      String guidStr = guid.toString();
      if (vid != 0 && pid != 0) {
         CUtil.LOGGER.log("Using SDL to identify controller type.");
         return Optional.of(new ControllerHIDService.ControllerHIDInfo(Controlify.instance().controllerTypeManager().getControllerType(new HIDIdentifier(vid, pid)), Optional.of(new HIDDevice.SDLHidApi(vid, pid, guidStr))));
      } else {
         return Optional.empty();
      }
   }

   private static class EventFilter implements SDL_EventFilter {
      public boolean filterEvent(Pointer userdata, SDL_Event event) {
         switch(event.type) {
         case 1541:
         case 1542:
            return true;
         default:
            return false;
         }
      }
   }

   public static record SDLUniqueControllerID(@NotNull SDL_JoystickID jid) implements UniqueControllerID {
      public SDLUniqueControllerID(@NotNull SDL_JoystickID jid) {
         this.jid = jid;
      }

      public boolean equals(Object obj) {
         return obj instanceof SDLControllerManager.SDLUniqueControllerID && ((SDLControllerManager.SDLUniqueControllerID)obj).jid.equals(this.jid);
      }

      public String toString() {
         return "SDL-" + this.jid.longValue();
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.jid.longValue()});
      }

      @NotNull
      public SDL_JoystickID jid() {
         return this.jid;
      }
   }
}
