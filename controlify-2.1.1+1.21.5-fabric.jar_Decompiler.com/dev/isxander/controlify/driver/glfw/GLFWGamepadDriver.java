package dev.isxander.controlify.driver.glfw;

import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.impl.ControllerStateImpl;
import dev.isxander.controlify.controller.info.DriverNameComponent;
import dev.isxander.controlify.controller.info.GUIDComponent;
import dev.isxander.controlify.controller.input.GamepadInputs;
import dev.isxander.controlify.controller.input.InputComponent;
import dev.isxander.controlify.driver.Driver;
import net.minecraft.class_3532;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;

public class GLFWGamepadDriver implements Driver {
   private final int jid;
   private final String guid;
   private final String name;
   private InputComponent inputComponent;

   public GLFWGamepadDriver(int jid) {
      this.jid = jid;
      this.guid = GLFW.glfwGetJoystickGUID(jid);
      this.name = GLFW.glfwGetGamepadName(jid);
      this.getGamepadState();
   }

   public void addComponents(ControllerEntity controller) {
      controller.setComponent(new DriverNameComponent(this.name));
      controller.setComponent(new GUIDComponent(this.guid));
      controller.setComponent(this.inputComponent = new InputComponent(controller, 15, 10, 0, true, GamepadInputs.DEADZONE_GROUPS, controller.info().type().mappingId()));
   }

   public void update(ControllerEntity controller, boolean outOfFocus) {
      this.updateInput();
   }

   public void close() {
   }

   private void updateInput() {
      GLFWGamepadState glfwState = this.getGamepadState();
      ControllerStateImpl state = new ControllerStateImpl();
      state.setAxis(GamepadInputs.LEFT_STICK_AXIS_DOWN, this.positiveAxis(glfwState.axes(1)));
      state.setAxis(GamepadInputs.LEFT_STICK_AXIS_RIGHT, this.positiveAxis(glfwState.axes(0)));
      state.setAxis(GamepadInputs.LEFT_STICK_AXIS_UP, this.negativeAxis(glfwState.axes(1)));
      state.setAxis(GamepadInputs.LEFT_STICK_AXIS_LEFT, this.negativeAxis(glfwState.axes(0)));
      state.setAxis(GamepadInputs.RIGHT_STICK_AXIS_UP, this.negativeAxis(glfwState.axes(3)));
      state.setAxis(GamepadInputs.RIGHT_STICK_AXIS_LEFT, this.negativeAxis(glfwState.axes(2)));
      state.setAxis(GamepadInputs.RIGHT_STICK_AXIS_DOWN, this.positiveAxis(glfwState.axes(3)));
      state.setAxis(GamepadInputs.RIGHT_STICK_AXIS_RIGHT, this.positiveAxis(glfwState.axes(2)));
      state.setAxis(GamepadInputs.LEFT_TRIGGER_AXIS, (1.0F + glfwState.axes(4)) / 2.0F);
      state.setAxis(GamepadInputs.RIGHT_TRIGGER_AXIS, (1.0F + glfwState.axes(5)) / 2.0F);
      state.setButton(GamepadInputs.SOUTH_BUTTON, glfwState.buttons(0) == 1);
      state.setButton(GamepadInputs.EAST_BUTTON, glfwState.buttons(1) == 1);
      state.setButton(GamepadInputs.WEST_BUTTON, glfwState.buttons(2) == 1);
      state.setButton(GamepadInputs.NORTH_BUTTON, glfwState.buttons(3) == 1);
      state.setButton(GamepadInputs.LEFT_SHOULDER_BUTTON, glfwState.buttons(4) == 1);
      state.setButton(GamepadInputs.RIGHT_SHOULDER_BUTTON, glfwState.buttons(5) == 1);
      state.setButton(GamepadInputs.LEFT_STICK_BUTTON, glfwState.buttons(9) == 1);
      state.setButton(GamepadInputs.RIGHT_STICK_BUTTON, glfwState.buttons(10) == 1);
      state.setButton(GamepadInputs.BACK_BUTTON, glfwState.buttons(6) == 1);
      state.setButton(GamepadInputs.START_BUTTON, glfwState.buttons(7) == 1);
      state.setButton(GamepadInputs.GUIDE_BUTTON, glfwState.buttons(8) == 1);
      state.setButton(GamepadInputs.DPAD_UP_BUTTON, glfwState.buttons(11) == 1);
      state.setButton(GamepadInputs.DPAD_DOWN_BUTTON, glfwState.buttons(13) == 1);
      state.setButton(GamepadInputs.DPAD_LEFT_BUTTON, glfwState.buttons(14) == 1);
      state.setButton(GamepadInputs.DPAD_RIGHT_BUTTON, glfwState.buttons(12) == 1);
      this.inputComponent.pushState(state);
   }

   private GLFWGamepadState getGamepadState() {
      GLFWGamepadState state = GLFWGamepadState.create();
      GLFW.glfwGetGamepadState(this.jid, state);
      return state;
   }

   private float positiveAxis(float value) {
      return value < 0.0F ? 0.0F : value;
   }

   private float negativeAxis(float value) {
      return value > 0.0F ? 0.0F : -value;
   }

   private static float mapShortToFloat(short value) {
      return class_3532.method_37958((float)value, -32768.0F, 0.0F, -1.0F, 0.0F) + class_3532.method_37958((float)value, 0.0F, 32767.0F, 0.0F, 1.0F);
   }
}
