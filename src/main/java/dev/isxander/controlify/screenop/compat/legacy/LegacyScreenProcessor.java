package dev.isxander.controlify.screenop.compat.legacy;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.input.ControllerStateView;
import dev.isxander.controlify.controller.input.GamepadInputs;
import dev.isxander.controlify.controller.input.InputComponent;
import dev.isxander.controlify.mixins.feature.screenop.ScreenAccessor;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.controlify.virtualmouse.VirtualMouseBehaviour;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.navigation.ScreenDirection;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.gui.screens.Screen;

/**
 * Generic processor for Legacy4J screens: delegates navigation to Legacy via keyPressed events.
 * This avoids double-handling (Controlify focus navigation + Legacy handling).
 */
public class LegacyScreenProcessor<T extends Screen> extends ScreenProcessor<T> {

    protected boolean initialFocusSet;
    public LegacyScreenProcessor(T screen) {
        super(screen);
        this.initialFocusSet = false;
    }

    @Override
    protected boolean handleComponentNavOverride(ControllerEntity controller) {
        // Skip any component-specific overrides; Legacy handles navigation globally.
        return false;
    }

    @Override
    public void setInitialFocus() {
        if(initialFocusSet) return;
        holdRepeatHelper.reset();
        super.setInitialFocus();
        initialFocusSet = true;
    }

    @Override
    public void onControllerUpdate(ControllerEntity controller) {
        Controlify.instance().virtualMouseHandler().handleControllerInput(controller);

        if (shouldHandleComponentNavigation()) {
            if (!handleComponentNavOverride(controller))
                handleComponentNavigation(controller);
        }

        if (!handleComponentButtonOverride(controller))
            handleButtons(controller);

        if (Controlify.instance().virtualMouseHandler().isVirtualMouseEnabled())
            handleScreenVMouse(controller, Controlify.instance().virtualMouseHandler());

        handleTabNavigation(controller);

        super.eventListeners.forEach(listener -> listener.onControllerInput(controller));
    }

    @Override
    protected void handleComponentNavigation(ControllerEntity controller) {
        // Only emit keyboard events and let Legacy handle. No focus path changes here.
        boolean repeatEventAvailable = holdRepeatHelper.canNavigate();

        InputComponent input = controller.input().orElseThrow();
        ControllerStateView state = input.stateNow();
        ControllerStateView prevState = input.stateThen();

        var right = ControlifyBindings.GUI_NAVI_RIGHT.on(controller);
        var left  = ControlifyBindings.GUI_NAVI_LEFT.on(controller);
        var up    = ControlifyBindings.GUI_NAVI_UP.on(controller);
        var down  = ControlifyBindings.GUI_NAVI_DOWN.on(controller);

        LegacyKeyBinding binding = null;
        if (right.digitalNow() && (repeatEventAvailable || !right.digitalPrev())) {
            binding = new LegacyKeyBinding(GLFW.GLFW_KEY_RIGHT, 0, 0);
            if (!right.digitalPrev()) holdRepeatHelper.reset();
        } else if (left.digitalNow() && (repeatEventAvailable || !left.digitalPrev())) {
            binding = new LegacyKeyBinding(GLFW.GLFW_KEY_LEFT, 0, 0);
            if (!left.digitalPrev()) holdRepeatHelper.reset();
        } else if (up.digitalNow() && (repeatEventAvailable || !up.digitalPrev())) {
            binding = new LegacyKeyBinding(GLFW.GLFW_KEY_UP, 0, 0);
            if (!up.digitalPrev()) holdRepeatHelper.reset();
        } else if (down.digitalNow() && (repeatEventAvailable || !down.digitalPrev())) {
            binding = new LegacyKeyBinding(GLFW.GLFW_KEY_DOWN, 0, 0);
            if (!down.digitalPrev()) holdRepeatHelper.reset();
        } else if (state.isButtonDown(GamepadInputs.DPAD_RIGHT_BUTTON) && (repeatEventAvailable || !prevState.isButtonDown(GamepadInputs.DPAD_RIGHT_BUTTON))) {
            binding = new LegacyKeyBinding(GLFW.GLFW_KEY_RIGHT, 0, 0);
            if (!prevState.isButtonDown(GamepadInputs.DPAD_RIGHT_BUTTON)) holdRepeatHelper.reset();
        } else if (state.isButtonDown(GamepadInputs.DPAD_LEFT_BUTTON) && (repeatEventAvailable || !prevState.isButtonDown(GamepadInputs.DPAD_LEFT_BUTTON))) {
            binding = new LegacyKeyBinding(GLFW.GLFW_KEY_LEFT, 0, 0);
            if (!prevState.isButtonDown(GamepadInputs.DPAD_LEFT_BUTTON)) holdRepeatHelper.reset();
        } else if (state.isButtonDown(GamepadInputs.DPAD_UP_BUTTON) && (repeatEventAvailable || !prevState.isButtonDown(GamepadInputs.DPAD_UP_BUTTON))) {
            binding = new LegacyKeyBinding(GLFW.GLFW_KEY_UP, 0, 0);
            if (!prevState.isButtonDown(GamepadInputs.DPAD_UP_BUTTON)) holdRepeatHelper.reset();
        } else if (state.isButtonDown(GamepadInputs.DPAD_DOWN_BUTTON) && (repeatEventAvailable || !prevState.isButtonDown(GamepadInputs.DPAD_DOWN_BUTTON))) {
            binding = new LegacyKeyBinding(GLFW.GLFW_KEY_DOWN, 0, 0);
            if (!prevState.isButtonDown(GamepadInputs.DPAD_DOWN_BUTTON)) holdRepeatHelper.reset();
        }

        if (binding != null) {
            screen.keyPressed(binding.key(), binding.scan(), binding.mods());
            holdRepeatHelper.onNavigate();
        }
    }
    record LegacyKeyBinding(int key, int scan, int mods) {}
}
