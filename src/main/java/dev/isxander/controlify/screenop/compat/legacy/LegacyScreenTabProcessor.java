package dev.isxander.controlify.screenop.compat.legacy;

import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.input.InputComponent;
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

        var next = ControlifyBindings.GUI_NEXT_TAB.on(controller);
        var prev  = ControlifyBindings.GUI_PREV_TAB.on(controller);
        var rt = ControlifyBindings.GUI_TRIG_RIGHT.on(controller);
        var lt = ControlifyBindings.GUI_TRIG_LEFT.on(controller);

        LegacyKeyBinding binding = null;
        if (next.digitalNow() && (repeatEventAvailable || !next.digitalPrev())) {
            binding = new LegacyKeyBinding(GLFW.GLFW_KEY_RIGHT_BRACKET, 0, 0);
            if (!next.digitalPrev()) holdRepeatHelper.reset();
        } else if (prev.digitalNow() && (repeatEventAvailable || !prev.digitalPrev())) {
            binding = new LegacyKeyBinding(GLFW.GLFW_KEY_LEFT_BRACKET, 0, 0);
            if (!prev.digitalPrev()) holdRepeatHelper.reset();
        } else if (rt.digitalNow() && (repeatEventAvailable || !rt.digitalPrev())) {
            binding = new LegacyKeyBinding(GLFW.GLFW_KEY_RIGHT_BRACKET, 0, GLFW.GLFW_MOD_SHIFT);
            if (!rt.digitalPrev()) holdRepeatHelper.reset();
        } else if (lt.digitalNow() && (repeatEventAvailable || !lt.digitalPrev())) {
            binding = new LegacyKeyBinding(GLFW.GLFW_KEY_LEFT_BRACKET, 0, GLFW.GLFW_MOD_SHIFT);
            if (!lt.digitalPrev()) holdRepeatHelper.reset();
        }
        if (binding != null) {
            screen.keyPressed(binding.key(), binding.scan(), binding.mods());
            holdRepeatHelper.onNavigate();
        }
    }
}
