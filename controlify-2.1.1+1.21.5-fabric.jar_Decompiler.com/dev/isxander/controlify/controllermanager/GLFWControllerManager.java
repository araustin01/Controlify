package dev.isxander.controlify.controllermanager;

import com.google.common.io.ByteStreams;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.info.ControllerInfo;
import dev.isxander.controlify.controller.info.UIDComponent;
import dev.isxander.controlify.debug.DebugProperties;
import dev.isxander.controlify.driver.ComponentAdderDriver;
import dev.isxander.controlify.driver.CompoundDriver;
import dev.isxander.controlify.driver.Driver;
import dev.isxander.controlify.driver.glfw.GLFWGamepadDriver;
import dev.isxander.controlify.driver.glfw.GLFWJoystickDriver;
import dev.isxander.controlify.driver.steamdeck.SteamDeckDriver;
import dev.isxander.controlify.driver.steamdeck.SteamDeckUtil;
import dev.isxander.controlify.hid.ControllerHIDService;
import dev.isxander.controlify.hid.HIDDevice;
import dev.isxander.controlify.hid.HIDIdentifier;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.controlify.utils.log.ControlifyLogger;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import net.minecraft.class_3298;
import net.minecraft.class_5912;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

public class GLFWControllerManager extends AbstractControllerManager {
   private boolean steamDeckConsumed = false;

   public GLFWControllerManager(ControlifyLogger logger) {
      super(logger);
      this.setupCallbacks();
   }

   private void setupCallbacks() {
      GLFW.glfwSetJoystickCallback((jid, event) -> {
         try {
            GLFWControllerManager.GLFWUniqueControllerID ucid = new GLFWControllerManager.GLFWUniqueControllerID(jid);
            if (event == 262145) {
               this.tryCreate(ucid, this.controlify.controllerHIDService().fetchType(jid)).ifPresent((controller) -> {
                  this.onControllerConnected(controller, true);
               });
            } else if (event == 262146) {
               this.getController(ucid).ifPresent(this::onControllerRemoved);
            }
         } catch (Throwable var4) {
            CUtil.LOGGER.error("Failed to handle controller connect/disconnect event", var4);
         }

      });
   }

   public void discoverControllers() {
      for(int i = 0; i < 15; ++i) {
         if (GLFW.glfwJoystickPresent(i)) {
            UniqueControllerID ucid = new GLFWControllerManager.GLFWUniqueControllerID(i);
            Optional<ControllerEntity> controllerOpt = this.tryCreate(ucid, this.controlify.controllerHIDService().fetchType(i));
            controllerOpt.ifPresent((controller) -> {
               this.onControllerConnected(controller, false);
            });
         }
      }

   }

   protected Optional<ControllerEntity> createController(UniqueControllerID ucid, ControllerHIDService.ControllerHIDInfo hidInfo, ControlifyLogger controllerLogger) {
      int jid = ((GLFWControllerManager.GLFWUniqueControllerID)ucid).jid;
      boolean isGamepad = this.isControllerGamepad(ucid) && !DebugProperties.FORCE_JOYSTICK;
      List<Driver> drivers = new ArrayList();
      Optional hid;
      if (SteamDeckUtil.DECK_MODE.isGamingMode() && !this.steamDeckConsumed && hidInfo.type().namespace().equals(SteamDeckUtil.STEAM_DECK_NAMESPACE)) {
         hid = SteamDeckDriver.create(controllerLogger);
         if (hid.isPresent()) {
            drivers.add((Driver)hid.get());
            this.steamDeckConsumed = true;
         }
      }

      if (isGamepad) {
         drivers.add(new GLFWGamepadDriver(jid));
      } else {
         drivers.add(new GLFWJoystickDriver(jid));
      }

      hid = hidInfo.hidDevice().map(HIDDevice::asIdentifier);
      String uid = (String)hidInfo.createControllerUID(this.getControllerCountWithMatchingHID((HIDIdentifier)hid.orElse((Object)null))).orElse("unknown-uid-" + String.valueOf(ucid));
      drivers.add(new ComponentAdderDriver((controllerx) -> {
         controllerx.setComponent(new UIDComponent(uid));
      }));
      CompoundDriver compoundDriver = new CompoundDriver(drivers);
      ControllerInfo info = new ControllerInfo(ucid, hidInfo.type(), hidInfo.hidDevice());
      ControllerEntity controller = new ControllerEntity(info, compoundDriver, controllerLogger);
      this.addController(ucid, controller);
      return Optional.of(controller);
   }

   public boolean probeConnectedControllers() {
      return areControllersConnected();
   }

   protected void loadGamepadMappings(class_5912 resourceProvider) {
      CUtil.LOGGER.debugLog("Loading gamepad mappings...");
      Optional<class_3298> resourceOpt = resourceProvider.method_14486(CUtil.rl("controllers/gamecontrollerdb-sdl2.txt"));
      if (resourceOpt.isEmpty()) {
         CUtil.LOGGER.error("Failed to find game controller database.");
      } else {
         try {
            InputStream is = ((class_3298)resourceOpt.get()).method_14482();

            try {
               byte[] bytes = ByteStreams.toByteArray(is);
               ByteBuffer buffer = MemoryUtil.memASCIISafe(new String(bytes));
               if (!GLFW.glfwUpdateGamepadMappings(buffer)) {
                  CUtil.LOGGER.error("Failed to load gamepad mappings: {}", GLFW.glfwGetError((PointerBuffer)null));
               }
            } catch (Throwable var7) {
               if (is != null) {
                  try {
                     is.close();
                  } catch (Throwable var6) {
                     var7.addSuppressed(var6);
                  }
               }

               throw var7;
            }

            if (is != null) {
               is.close();
            }
         } catch (Throwable var8) {
            CUtil.LOGGER.error("Failed to load gamepad mappings: {}", var8.getMessage());
         }

      }
   }

   private Optional<ControllerEntity> getController(GLFWControllerManager.GLFWUniqueControllerID joystickId) {
      return this.controllersByUid.values().stream().filter((controller) -> {
         return controller.info().ucid().equals(joystickId);
      }).findAny();
   }

   public boolean isControllerGamepad(UniqueControllerID ucid) {
      int joystickId = ((GLFWControllerManager.GLFWUniqueControllerID)ucid).jid;
      return GLFW.glfwJoystickIsGamepad(joystickId);
   }

   protected String getControllerSystemName(UniqueControllerID ucid) {
      int joystickId = ((GLFWControllerManager.GLFWUniqueControllerID)ucid).jid;
      return this.isControllerGamepad(ucid) ? GLFW.glfwGetGamepadName(joystickId) : GLFW.glfwGetJoystickName(joystickId);
   }

   public static boolean areControllersConnected() {
      return IntStream.range(0, 16).anyMatch(GLFW::glfwJoystickPresent);
   }

   public static record GLFWUniqueControllerID(int jid) implements UniqueControllerID {
      public GLFWUniqueControllerID(int jid) {
         this.jid = jid;
      }

      public int jid() {
         return this.jid;
      }
   }
}
