package dev.isxander.controlify.screenop.compat.legacy;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.input.ControllerStateView;
import dev.isxander.controlify.controller.input.GamepadInputs;
import dev.isxander.controlify.controller.input.InputComponent;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.utils.CUtil;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.gui.screens.Screen;

/**
 * Generic processor for Legacy4J screens: delegates navigation to Legacy via keyPressed events.
 * This avoids double-handling (Controlify focus navigation + Legacy handling).
 */
public class LegacyScreenProcessor<T extends Screen> extends ScreenProcessor<T> {
    public LegacyScreenProcessor(T screen) {
        super(screen);
    }

    @Override
    protected boolean handleComponentNavOverride(ControllerEntity controller) {
        // Skip any component-specific overrides; Legacy handles navigation globally.
        return false;
    }

    @Override
    public void setInitialFocus() {
        // No-op: let Legacy4J manage focus entirely.
    }

    @Override
    protected void handleComponentNavigation(ControllerEntity controller) {
        CUtil.LOGGER.log(getClass().getSimpleName() + " handling component navigation for " + screen.getClass().getSimpleName());
        // Only emit keyboard events and let Legacy handle. No focus path changes here.
        boolean repeatEventAvailable = holdRepeatHelper.canNavigate();

        InputComponent input = controller.input().orElseThrow();
        ControllerStateView state = input.stateNow();
        ControllerStateView prevState = input.stateThen();

        var right = ControlifyBindings.GUI_NAVI_RIGHT.on(controller);
        var left  = ControlifyBindings.GUI_NAVI_LEFT.on(controller);
        var up    = ControlifyBindings.GUI_NAVI_UP.on(controller);
        var down  = ControlifyBindings.GUI_NAVI_DOWN.on(controller);

        Integer key = null;
        if (right.digitalNow() && (repeatEventAvailable || !right.digitalPrev())) {
            key = GLFW.GLFW_KEY_RIGHT;
            if (!right.digitalPrev()) holdRepeatHelper.reset();
        } else if (left.digitalNow() && (repeatEventAvailable || !left.digitalPrev())) {
            key = GLFW.GLFW_KEY_LEFT;
            if (!left.digitalPrev()) holdRepeatHelper.reset();
        } else if (up.digitalNow() && (repeatEventAvailable || !up.digitalPrev())) {
            key = GLFW.GLFW_KEY_UP;
            if (!up.digitalPrev()) holdRepeatHelper.reset();
        } else if (down.digitalNow() && (repeatEventAvailable || !down.digitalPrev())) {
            key = GLFW.GLFW_KEY_DOWN;
            if (!down.digitalPrev()) holdRepeatHelper.reset();
        } else if (state.isButtonDown(GamepadInputs.DPAD_RIGHT_BUTTON) && (repeatEventAvailable || !prevState.isButtonDown(GamepadInputs.DPAD_RIGHT_BUTTON))) {
            key = GLFW.GLFW_KEY_RIGHT;
            if (!prevState.isButtonDown(GamepadInputs.DPAD_RIGHT_BUTTON)) holdRepeatHelper.reset();
        } else if (state.isButtonDown(GamepadInputs.DPAD_LEFT_BUTTON) && (repeatEventAvailable || !prevState.isButtonDown(GamepadInputs.DPAD_LEFT_BUTTON))) {
            key = GLFW.GLFW_KEY_LEFT;
            if (!prevState.isButtonDown(GamepadInputs.DPAD_LEFT_BUTTON)) holdRepeatHelper.reset();
        } else if (state.isButtonDown(GamepadInputs.DPAD_UP_BUTTON) && (repeatEventAvailable || !prevState.isButtonDown(GamepadInputs.DPAD_UP_BUTTON))) {
            key = GLFW.GLFW_KEY_UP;
            if (!prevState.isButtonDown(GamepadInputs.DPAD_UP_BUTTON)) holdRepeatHelper.reset();
        } else if (state.isButtonDown(GamepadInputs.DPAD_DOWN_BUTTON) && (repeatEventAvailable || !prevState.isButtonDown(GamepadInputs.DPAD_DOWN_BUTTON))) {
            key = GLFW.GLFW_KEY_DOWN;
            if (!prevState.isButtonDown(GamepadInputs.DPAD_DOWN_BUTTON)) holdRepeatHelper.reset();
        }

        if (key != null) {
            screen.keyPressed(key, 0, 0);
            holdRepeatHelper.onNavigate();
        }
    }
}
