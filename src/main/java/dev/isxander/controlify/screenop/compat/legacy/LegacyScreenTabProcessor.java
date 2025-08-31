package dev.isxander.controlify.screenop.compat.legacy;

import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.input.ControllerStateView;
import dev.isxander.controlify.controller.input.GamepadInputs;
import dev.isxander.controlify.controller.input.InputComponent;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.utils.CUtil;
import net.minecraft.client.gui.screens.Screen;
import org.lwjgl.glfw.GLFW;

/**
 * Generic processor for Legacy4J screens: delegates navigation to Legacy via keyPressed events.
 * This avoids double-handling (Controlify focus navigation + Legacy handling).
 */
public class LegacyScreenTabProcessor<T extends Screen> extends LegacyScreenProcessor<T> {
    public LegacyScreenTabProcessor(T screen) {
        super(screen);
    }

    @Override
    protected void handleComponentNavigation(ControllerEntity controller) {
        super.handleComponentNavigation(controller);
        // Only emit keyboard events and let Legacy handle. No focus path changes here.
        boolean repeatEventAvailable = holdRepeatHelper.canNavigate();

        InputComponent input = controller.input().orElseThrow();
        ControllerStateView state = input.stateNow();
        ControllerStateView prevState = input.stateThen();

        var next = ControlifyBindings.GUI_NEXT_TAB.on(controller);
        var prev  = ControlifyBindings.GUI_PREV_TAB.on(controller);
        var up    = ControlifyBindings.VMOUSE_MOVE_UP.on(controller);
        var down  = ControlifyBindings.VMOUSE_MOVE_DOWN.on(controller);

        Integer key = null;
        if (next.digitalNow() && (repeatEventAvailable || !next.digitalPrev())) {
            key = GLFW.GLFW_KEY_RIGHT_BRACKET;
            if (!next.digitalPrev()) holdRepeatHelper.reset();
        } else if (prev.digitalNow() && (repeatEventAvailable || !prev.digitalPrev())) {
            key = GLFW.GLFW_KEY_LEFT_BRACKET;
            if (!prev.digitalPrev()) holdRepeatHelper.reset();
        } else if (up.digitalNow() && (repeatEventAvailable || !up.digitalPrev())) {
            key = GLFW.GLFW_KEY_PAGE_UP;
            if (!up.digitalPrev()) holdRepeatHelper.reset();
        } else if (down.digitalNow() && (repeatEventAvailable || !down.digitalPrev())) {
            key = GLFW.GLFW_KEY_PAGE_DOWN;
            if (!down.digitalPrev()) holdRepeatHelper.reset();
        }

        if (key != null) {
            screen.keyPressed(key, 0, 0);
            holdRepeatHelper.onNavigate();
        }
    }
}
