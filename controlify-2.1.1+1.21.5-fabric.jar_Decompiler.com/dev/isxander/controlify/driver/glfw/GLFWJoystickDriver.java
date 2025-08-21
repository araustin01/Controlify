package dev.isxander.controlify.driver.glfw;

import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.impl.ControllerStateImpl;
import dev.isxander.controlify.controller.info.DriverNameComponent;
import dev.isxander.controlify.controller.info.GUIDComponent;
import dev.isxander.controlify.controller.input.HatState;
import dev.isxander.controlify.controller.input.InputComponent;
import dev.isxander.controlify.controller.input.JoystickInputs;
import dev.isxander.controlify.driver.Driver;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Set;
import org.apache.commons.lang3.Validate;
import org.lwjgl.glfw.GLFW;

public class GLFWJoystickDriver implements Driver {
   private final int jid;
   private final String guid;
   private final String name;
   private final int numButtons;
   private final int numAxes;
   private final int numHats;
   private InputComponent inputComponent;

   public GLFWJoystickDriver(int jid) {
      this.jid = jid;
      this.guid = GLFW.glfwGetJoystickGUID(jid);
      this.name = GLFW.glfwGetJoystickName(jid);
      GLFWJoystickDriver.GLFWJoystickState testState = this.getJoystickState();
      this.numButtons = testState.buttons().limit();
      this.numAxes = testState.axes().limit();
      this.numHats = testState.hats().limit();
   }

   public void addComponents(ControllerEntity controller) {
      controller.setComponent(new DriverNameComponent(this.name));
      controller.setComponent(new GUIDComponent(this.guid));
      controller.setComponent(this.inputComponent = new InputComponent(controller, this.numButtons, this.numAxes * 2, this.numHats, false, Set.of(), controller.info().type().mappingId()));
   }

   public void update(ControllerEntity controller, boolean outOfFocus) {
      this.updateInput();
   }

   public void close() {
   }

   private void updateInput() {
      GLFWJoystickDriver.GLFWJoystickState glfwState = this.getJoystickState();
      ControllerStateImpl state = new ControllerStateImpl();

      int i;
      for(i = 0; i < this.numAxes; ++i) {
         float axis = glfwState.axes().get(i);
         state.setAxis(JoystickInputs.axis(i, true), Math.max(axis, 0.0F));
         state.setAxis(JoystickInputs.axis(i, false), -Math.min(axis, 0.0F));
      }

      for(i = 0; i < this.numButtons; ++i) {
         state.setButton(JoystickInputs.button(i), glfwState.buttons().get(i) == 1);
      }

      for(i = 0; i < this.numHats; ++i) {
         HatState var10000;
         switch(glfwState.hats().get(i)) {
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
            throw new IllegalStateException("Unexpected value: " + glfwState.hats().get(i));
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

   private GLFWJoystickDriver.GLFWJoystickState getJoystickState() {
      ByteBuffer buttonsBuf = GLFW.glfwGetJoystickButtons(this.jid);
      FloatBuffer axesBuf = GLFW.glfwGetJoystickAxes(this.jid);
      ByteBuffer hatsBuf = GLFW.glfwGetJoystickHats(this.jid);
      Validate.notNull(buttonsBuf, "Could not fetch buttons state for joystick", new Object[0]);
      Validate.notNull(axesBuf, "Could not fetch axes state for joystick", new Object[0]);
      Validate.notNull(hatsBuf, "Could not fetch  hat state for joystick", new Object[0]);
      return new GLFWJoystickDriver.GLFWJoystickState(buttonsBuf, axesBuf, hatsBuf);
   }

   private static record GLFWJoystickState(ByteBuffer buttons, FloatBuffer axes, ByteBuffer hats) {
      private GLFWJoystickState(ByteBuffer buttons, FloatBuffer axes, ByteBuffer hats) {
         this.buttons = buttons;
         this.axes = axes;
         this.hats = hats;
      }

      public ByteBuffer buttons() {
         return this.buttons;
      }

      public FloatBuffer axes() {
         return this.axes;
      }

      public ByteBuffer hats() {
         return this.hats;
      }
   }
}
