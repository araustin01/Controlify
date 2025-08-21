package dev.isxander.controlify.driver.sdl;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.IntByReference;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.gyro.GyroComponent;
import dev.isxander.controlify.controller.gyro.GyroState;
import dev.isxander.controlify.controller.id.ControllerType;
import dev.isxander.controlify.controller.impl.ControllerStateImpl;
import dev.isxander.controlify.controller.input.GamepadInputs;
import dev.isxander.controlify.controller.input.InputComponent;
import dev.isxander.controlify.controller.touchpad.TouchpadComponent;
import dev.isxander.controlify.controller.touchpad.Touchpads;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.controlify.utils.log.ControlifyLogger;
import dev.isxander.sdl3java.api.error.SdlError;
import dev.isxander.sdl3java.api.gamepad.SDL_Gamepad;
import dev.isxander.sdl3java.api.gamepad.SdlGamepad;
import dev.isxander.sdl3java.api.joystick.SDL_JoystickGUID;
import dev.isxander.sdl3java.api.joystick.SDL_JoystickID;
import dev.isxander.sdl3java.api.properties.SDL_PropertiesID;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.joml.Vector2f;

public class SDL3GamepadDriver extends SDLCommonDriver<SDL_Gamepad> {
   private InputComponent inputComponent;
   private GyroComponent gyroComponent;
   private TouchpadComponent touchpadComponent;
   private final boolean isGryoSupported;
   private final int numTouchpads;

   public SDL3GamepadDriver(SDL_Gamepad ptrController, SDL_JoystickID jid, ControllerType type, ControlifyLogger logger) {
      super(ptrController, jid, type, logger);
      this.isGryoSupported = SdlGamepad.SDL_GamepadHasSensor(ptrController, 2);
      this.numTouchpads = SdlGamepad.SDL_GetNumGamepadTouchpads(ptrController);
      if (this.isGryoSupported) {
         SdlGamepad.SDL_SetGamepadSensorEnabled(ptrController, 2, true);
      }

   }

   public void addComponents(ControllerEntity controller) {
      super.addComponents(controller);
      controller.setComponent(this.inputComponent = new InputComponent(controller, 21, 10, 0, true, GamepadInputs.DEADZONE_GROUPS, controller.info().type().mappingId()));
      if (this.isGryoSupported) {
         controller.setComponent(this.gyroComponent = new GyroComponent());
      }

      if (this.numTouchpads > 0) {
         controller.setComponent(this.touchpadComponent = new TouchpadComponent(new Touchpads((Touchpads.Touchpad[])IntStream.range(0, this.numTouchpads).mapToObj((i) -> {
            return new Touchpads.Touchpad(SdlGamepad.SDL_GetNumGamepadTouchpadFingers((SDL_Gamepad)this.ptrController, i));
         }).toArray((x$0) -> {
            return new Touchpads.Touchpad[x$0];
         }))));
      }

   }

   public void update(ControllerEntity controller, boolean outOfFocus) {
      super.update(controller, outOfFocus);
      this.updateInput();
      this.updateGyro();
      this.updateTouchpad();
   }

   private void updateInput() {
      ControllerStateImpl state = new ControllerStateImpl();
      state.setAxis(GamepadInputs.LEFT_STICK_AXIS_RIGHT, CUtil.positiveAxis(CUtil.mapShortToFloat(SdlGamepad.SDL_GetGamepadAxis((SDL_Gamepad)this.ptrController, 0))));
      state.setAxis(GamepadInputs.LEFT_STICK_AXIS_LEFT, CUtil.negativeAxis(CUtil.mapShortToFloat(SdlGamepad.SDL_GetGamepadAxis((SDL_Gamepad)this.ptrController, 0))));
      state.setAxis(GamepadInputs.LEFT_STICK_AXIS_UP, CUtil.negativeAxis(CUtil.mapShortToFloat(SdlGamepad.SDL_GetGamepadAxis((SDL_Gamepad)this.ptrController, 1))));
      state.setAxis(GamepadInputs.LEFT_STICK_AXIS_DOWN, CUtil.positiveAxis(CUtil.mapShortToFloat(SdlGamepad.SDL_GetGamepadAxis((SDL_Gamepad)this.ptrController, 1))));
      state.setAxis(GamepadInputs.RIGHT_STICK_AXIS_RIGHT, CUtil.positiveAxis(CUtil.mapShortToFloat(SdlGamepad.SDL_GetGamepadAxis((SDL_Gamepad)this.ptrController, 2))));
      state.setAxis(GamepadInputs.RIGHT_STICK_AXIS_LEFT, CUtil.negativeAxis(CUtil.mapShortToFloat(SdlGamepad.SDL_GetGamepadAxis((SDL_Gamepad)this.ptrController, 2))));
      state.setAxis(GamepadInputs.RIGHT_STICK_AXIS_UP, CUtil.negativeAxis(CUtil.mapShortToFloat(SdlGamepad.SDL_GetGamepadAxis((SDL_Gamepad)this.ptrController, 3))));
      state.setAxis(GamepadInputs.RIGHT_STICK_AXIS_DOWN, CUtil.positiveAxis(CUtil.mapShortToFloat(SdlGamepad.SDL_GetGamepadAxis((SDL_Gamepad)this.ptrController, 3))));
      state.setAxis(GamepadInputs.LEFT_TRIGGER_AXIS, CUtil.mapShortToFloat(SdlGamepad.SDL_GetGamepadAxis((SDL_Gamepad)this.ptrController, 4)));
      state.setAxis(GamepadInputs.RIGHT_TRIGGER_AXIS, CUtil.mapShortToFloat(SdlGamepad.SDL_GetGamepadAxis((SDL_Gamepad)this.ptrController, 5)));
      state.setButton(GamepadInputs.SOUTH_BUTTON, SdlGamepad.SDL_GetGamepadButton((SDL_Gamepad)this.ptrController, 0));
      state.setButton(GamepadInputs.EAST_BUTTON, SdlGamepad.SDL_GetGamepadButton((SDL_Gamepad)this.ptrController, 1));
      state.setButton(GamepadInputs.WEST_BUTTON, SdlGamepad.SDL_GetGamepadButton((SDL_Gamepad)this.ptrController, 2));
      state.setButton(GamepadInputs.NORTH_BUTTON, SdlGamepad.SDL_GetGamepadButton((SDL_Gamepad)this.ptrController, 3));
      state.setButton(GamepadInputs.LEFT_SHOULDER_BUTTON, SdlGamepad.SDL_GetGamepadButton((SDL_Gamepad)this.ptrController, 9));
      state.setButton(GamepadInputs.RIGHT_SHOULDER_BUTTON, SdlGamepad.SDL_GetGamepadButton((SDL_Gamepad)this.ptrController, 10));
      state.setButton(GamepadInputs.BACK_BUTTON, SdlGamepad.SDL_GetGamepadButton((SDL_Gamepad)this.ptrController, 4));
      state.setButton(GamepadInputs.START_BUTTON, SdlGamepad.SDL_GetGamepadButton((SDL_Gamepad)this.ptrController, 6));
      state.setButton(GamepadInputs.GUIDE_BUTTON, SdlGamepad.SDL_GetGamepadButton((SDL_Gamepad)this.ptrController, 5));
      state.setButton(GamepadInputs.DPAD_UP_BUTTON, SdlGamepad.SDL_GetGamepadButton((SDL_Gamepad)this.ptrController, 11));
      state.setButton(GamepadInputs.DPAD_DOWN_BUTTON, SdlGamepad.SDL_GetGamepadButton((SDL_Gamepad)this.ptrController, 12));
      state.setButton(GamepadInputs.DPAD_LEFT_BUTTON, SdlGamepad.SDL_GetGamepadButton((SDL_Gamepad)this.ptrController, 13));
      state.setButton(GamepadInputs.DPAD_RIGHT_BUTTON, SdlGamepad.SDL_GetGamepadButton((SDL_Gamepad)this.ptrController, 14));
      state.setButton(GamepadInputs.LEFT_STICK_BUTTON, SdlGamepad.SDL_GetGamepadButton((SDL_Gamepad)this.ptrController, 7));
      state.setButton(GamepadInputs.RIGHT_STICK_BUTTON, SdlGamepad.SDL_GetGamepadButton((SDL_Gamepad)this.ptrController, 8));
      state.setButton(GamepadInputs.MISC_1_BUTTON, SdlGamepad.SDL_GetGamepadButton((SDL_Gamepad)this.ptrController, 15));
      state.setButton(GamepadInputs.MISC_2_BUTTON, SdlGamepad.SDL_GetGamepadButton((SDL_Gamepad)this.ptrController, 21));
      state.setButton(GamepadInputs.MISC_3_BUTTON, SdlGamepad.SDL_GetGamepadButton((SDL_Gamepad)this.ptrController, 22));
      state.setButton(GamepadInputs.MISC_4_BUTTON, SdlGamepad.SDL_GetGamepadButton((SDL_Gamepad)this.ptrController, 23));
      state.setButton(GamepadInputs.MISC_5_BUTTON, SdlGamepad.SDL_GetGamepadButton((SDL_Gamepad)this.ptrController, 24));
      state.setButton(GamepadInputs.MISC_6_BUTTON, SdlGamepad.SDL_GetGamepadButton((SDL_Gamepad)this.ptrController, 25));
      state.setButton(GamepadInputs.LEFT_PADDLE_1_BUTTON, SdlGamepad.SDL_GetGamepadButton((SDL_Gamepad)this.ptrController, 17));
      state.setButton(GamepadInputs.LEFT_PADDLE_2_BUTTON, SdlGamepad.SDL_GetGamepadButton((SDL_Gamepad)this.ptrController, 19));
      state.setButton(GamepadInputs.RIGHT_PADDLE_1_BUTTON, SdlGamepad.SDL_GetGamepadButton((SDL_Gamepad)this.ptrController, 16));
      state.setButton(GamepadInputs.RIGHT_PADDLE_2_BUTTON, SdlGamepad.SDL_GetGamepadButton((SDL_Gamepad)this.ptrController, 18));
      state.setButton(GamepadInputs.TOUCHPAD_1_BUTTON, SdlGamepad.SDL_GetGamepadButton((SDL_Gamepad)this.ptrController, 20));
      this.inputComponent.pushState(state);
   }

   private void updateGyro() {
      if (this.isGryoSupported) {
         float[] gyro = new float[3];
         Memory memory = new Memory((long)(gyro.length * 4));

         try {
            if (SdlGamepad.SDL_GetGamepadSensorData((SDL_Gamepad)this.ptrController, 2, memory, 3)) {
               memory.read(0L, gyro, 0, gyro.length);
               this.gyroComponent.setState(new GyroState(gyro[0], gyro[1], gyro[2]));
            } else {
               CUtil.LOGGER.error("Could not get gyro data: {}", SdlError.SDL_GetError());
            }
         } catch (Throwable var6) {
            try {
               memory.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }

            throw var6;
         }

         memory.close();
      }
   }

   private void updateTouchpad() {
      if (this.numTouchpads >= 1) {
         for(int touchpadIdx = 0; touchpadIdx < this.numTouchpads; ++touchpadIdx) {
            Touchpads.Touchpad touchpad = this.touchpadComponent.touchpads()[touchpadIdx];
            List<Touchpads.Finger> fingers = new ArrayList();

            for(int fingerIdx = 0; fingerIdx < touchpad.maxFingers(); ++fingerIdx) {
               ByteByReference fingerState = new ByteByReference();
               FloatByReference x = new FloatByReference();
               FloatByReference y = new FloatByReference();
               FloatByReference pressure = new FloatByReference();
               if (!SdlGamepad.SDL_GetGamepadTouchpadFinger((SDL_Gamepad)this.ptrController, touchpadIdx, fingerIdx, fingerState, x, y, pressure)) {
                  CUtil.LOGGER.error("Failed to fetch touchpad finger: {}", SdlError.SDL_GetError());
               } else if (fingerState.getValue() == 1) {
                  fingers.add(new Touchpads.Finger(fingerIdx, new Vector2f(x.getValue(), y.getValue()), pressure.getValue()));
               }
            }

            touchpad.pushFingers(fingers);
         }

      }
   }

   protected SDL_PropertiesID SDL_GetControllerProperties(SDL_Gamepad ptrController) {
      return SdlGamepad.SDL_GetGamepadProperties(ptrController);
   }

   protected String SDL_GetControllerName(SDL_Gamepad ptrController) {
      return SdlGamepad.SDL_GetGamepadName(ptrController);
   }

   protected SDL_JoystickGUID SDL_GetControllerGUIDForID(SDL_JoystickID jid) {
      return SdlGamepad.SDL_GetGamepadGUIDForID(jid);
   }

   protected String SDL_GetControllerSerial(SDL_Gamepad ptrController) {
      return SdlGamepad.SDL_GetGamepadSerial(ptrController);
   }

   protected short SDL_GetControllerVendor(SDL_Gamepad ptrController) {
      return (short)SdlGamepad.SDL_GetGamepadVendor(ptrController);
   }

   protected short SDL_GetControllerProduct(SDL_Gamepad ptrController) {
      return (short)SdlGamepad.SDL_GetGamepadProduct(ptrController);
   }

   protected int SDL_GetControllerConnectionState(SDL_Gamepad ptrController) {
      return SdlGamepad.SDL_GetGamepadConnectionState(ptrController);
   }

   protected boolean SDL_CloseController(SDL_Gamepad ptrController) {
      return SdlGamepad.SDL_CloseGamepad(ptrController);
   }

   protected boolean SDL_RumbleController(SDL_Gamepad ptrController, float strong, float weak, int durationMs) {
      return SdlGamepad.SDL_RumbleGamepad(ptrController, (char)((int)(strong * 65535.0F)), (char)((int)(weak * 65535.0F)), (long)durationMs);
   }

   protected boolean SDL_RumbleControllerTriggers(SDL_Gamepad ptrController, float left, float right, int durationMs) {
      return SdlGamepad.SDL_RumbleGamepadTriggers(ptrController, (char)((int)(left * 65535.0F)), (char)((int)(right * 65535.0F)), (long)durationMs);
   }

   protected int SDL_GetControllerPowerInfo(SDL_Gamepad ptrController, IntByReference percent) {
      return SdlGamepad.SDL_GetGamepadPowerInfo(ptrController, percent);
   }

   protected boolean SDL_SendControllerEffect(SDL_Gamepad ptrController, Pointer effect, int size) {
      return SdlGamepad.SDL_SendGamepadEffect(ptrController, effect, size);
   }
}
