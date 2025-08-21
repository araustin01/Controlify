package dev.isxander.controlify.driver.sdl;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.id.ControllerType;
import dev.isxander.controlify.controller.impl.ControllerStateImpl;
import dev.isxander.controlify.controller.input.HatState;
import dev.isxander.controlify.controller.input.InputComponent;
import dev.isxander.controlify.controller.input.JoystickInputs;
import dev.isxander.controlify.utils.log.ControlifyLogger;
import dev.isxander.sdl3java.api.joystick.SDL_Joystick;
import dev.isxander.sdl3java.api.joystick.SDL_JoystickGUID;
import dev.isxander.sdl3java.api.joystick.SDL_JoystickID;
import dev.isxander.sdl3java.api.joystick.SdlJoystick;
import dev.isxander.sdl3java.api.properties.SDL_PropertiesID;
import java.util.Set;
import net.minecraft.class_3532;

public class SDL3JoystickDriver extends SDLCommonDriver<SDL_Joystick> {
   private InputComponent inputComponent;
   private final int numAxes;
   private final int numButtons;
   private final int numHats;

   public SDL3JoystickDriver(SDL_Joystick ptrJoystick, SDL_JoystickID jid, ControllerType type, ControlifyLogger logger) {
      super(ptrJoystick, jid, type, logger);
      this.numAxes = SdlJoystick.SDL_GetNumJoystickAxes(ptrJoystick);
      this.numButtons = SdlJoystick.SDL_GetNumJoystickButtons(ptrJoystick);
      this.numHats = SdlJoystick.SDL_GetNumJoystickHats(ptrJoystick);
   }

   public void addComponents(ControllerEntity controller) {
      super.addComponents(controller);
      controller.setComponent(this.inputComponent = new InputComponent(controller, this.numButtons, this.numAxes * 2, this.numHats, false, Set.of(), controller.info().type().mappingId()));
   }

   public void update(ControllerEntity controller, boolean outOfFocus) {
      super.update(controller, outOfFocus);
      this.updateInput();
   }

   private void updateInput() {
      ControllerStateImpl state = new ControllerStateImpl();

      int i;
      for(i = 0; i < this.numAxes; ++i) {
         float axis = mapShortToFloat(SdlJoystick.SDL_GetJoystickAxis((SDL_Joystick)this.ptrController, i));
         state.setAxis(JoystickInputs.axis(i, true), Math.max(axis, 0.0F));
         state.setAxis(JoystickInputs.axis(i, false), -Math.min(axis, 0.0F));
      }

      for(i = 0; i < this.numButtons; ++i) {
         state.setButton(JoystickInputs.button(i), SdlJoystick.SDL_GetJoystickButton((SDL_Joystick)this.ptrController, i) == 1);
      }

      for(i = 0; i < this.numHats; ++i) {
         HatState var10000;
         switch(SdlJoystick.SDL_GetJoystickHat((SDL_Joystick)this.ptrController, i)) {
         case 0:
            var10000 = HatState.CENTERED;
            break;
         case 1:
            var10000 = HatState.UP;
            break;
         case 2:
            var10000 = HatState.RIGHT;
            break;
         case 3:
            var10000 = HatState.RIGHT_UP;
            break;
         case 4:
            var10000 = HatState.DOWN;
            break;
         case 5:
         case 7:
         case 10:
         case 11:
         default:
            SDL_Joystick var10002 = (SDL_Joystick)this.ptrController;
            throw new IllegalStateException("Unexpected value: " + SdlJoystick.SDL_GetJoystickHat(var10002, i));
         case 6:
            var10000 = HatState.RIGHT_DOWN;
            break;
         case 8:
            var10000 = HatState.LEFT;
            break;
         case 9:
            var10000 = HatState.LEFT_UP;
            break;
         case 12:
            var10000 = HatState.LEFT_DOWN;
         }

         HatState hatState = var10000;
         state.setHat(JoystickInputs.hat(i), hatState);
      }

      this.inputComponent.pushState(state);
   }

   private static float mapShortToFloat(short value) {
      return class_3532.method_37958((float)value, -32768.0F, 0.0F, -1.0F, 0.0F) + class_3532.method_37958((float)value, 0.0F, 32767.0F, 0.0F, 1.0F);
   }

   protected SDL_PropertiesID SDL_GetControllerProperties(SDL_Joystick ptrController) {
      return SdlJoystick.SDL_GetJoystickProperties(ptrController);
   }

   protected String SDL_GetControllerName(SDL_Joystick ptrController) {
      return SdlJoystick.SDL_GetJoystickName(ptrController);
   }

   protected SDL_JoystickGUID SDL_GetControllerGUIDForID(SDL_JoystickID jid) {
      return SdlJoystick.SDL_GetJoystickGUIDForID(jid);
   }

   protected String SDL_GetControllerSerial(SDL_Joystick ptrController) {
      return SdlJoystick.SDL_GetJoystickSerial(ptrController);
   }

   protected short SDL_GetControllerVendor(SDL_Joystick ptrController) {
      return SdlJoystick.SDL_GetJoystickVendor(ptrController);
   }

   protected short SDL_GetControllerProduct(SDL_Joystick ptrController) {
      return SdlJoystick.SDL_GetJoystickProduct(ptrController);
   }

   protected int SDL_GetControllerConnectionState(SDL_Joystick ptrController) {
      return SdlJoystick.SDL_GetJoystickConnectionState(ptrController);
   }

   protected boolean SDL_CloseController(SDL_Joystick ptrController) {
      SdlJoystick.SDL_CloseJoystick(ptrController);
      return true;
   }

   protected boolean SDL_RumbleController(SDL_Joystick ptrController, float strong, float weak, int durationMs) {
      return SdlJoystick.SDL_RumbleJoystick(ptrController, (short)((int)(strong * 65535.0F)), (short)((int)(weak * 65535.0F)), durationMs);
   }

   protected boolean SDL_RumbleControllerTriggers(SDL_Joystick ptrController, float left, float right, int durationMs) {
      return SdlJoystick.SDL_RumbleJoystickTriggers(ptrController, (short)((int)(left * 65535.0F)), (short)((int)(right * 65535.0F)), durationMs);
   }

   protected int SDL_GetControllerPowerInfo(SDL_Joystick ptrController, IntByReference percent) {
      return SdlJoystick.SDL_GetJoystickPowerInfo(ptrController, percent);
   }

   protected boolean SDL_SendControllerEffect(SDL_Joystick ptrController, Pointer effect, int size) {
      return SdlJoystick.SDL_SendJoystickEffect(ptrController, effect, size);
   }
}
