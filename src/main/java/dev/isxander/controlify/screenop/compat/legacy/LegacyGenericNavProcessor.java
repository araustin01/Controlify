package dev.isxander.controlify.screenop.compat.legacy;

import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.utils.CUtil;
import net.minecraft.client.gui.components.events.GuiEventListener;
import org.lwjgl.glfw.GLFW;

/**
 * Generic fallback processor for Legacy4J components when no specific mapping exists.
 * Converts DPAD to arrow key presses and shoulders to mouse scroll on the component.
 */
public class LegacyGenericNavProcessor implements ComponentProcessor {
    private final GuiEventListener component;

    public LegacyGenericNavProcessor(GuiEventListener component) {
        this.component = component;
        CUtil.LOGGER.log("[LegacyCompat] Using LegacyGenericNavProcessor for: " + component.getClass().getName());
    }

    @Override
    public boolean overrideControllerNavigation(ScreenProcessor<?> screen, ControllerEntity controller) {
        if (ControlifyBindings.GUI_NAVI_LEFT.on(controller).justPressed()) {
            component.keyPressed(GLFW.GLFW_KEY_LEFT, 0, 0);
            return true;
        }
        if (ControlifyBindings.GUI_NAVI_RIGHT.on(controller).justPressed()) {
            component.keyPressed(GLFW.GLFW_KEY_RIGHT, 0, 0);
            return true;
        }
        if (ControlifyBindings.GUI_NAVI_UP.on(controller).justPressed()) {
            component.keyPressed(GLFW.GLFW_KEY_UP, 0, 0);
            return true;
        }
        if (ControlifyBindings.GUI_NAVI_DOWN.on(controller).justPressed()) {
            component.keyPressed(GLFW.GLFW_KEY_DOWN, 0, 0);
            return true;
        }
        if (ControlifyBindings.PREV_SLOT.on(controller).justPressed()) {
            component.mouseScrolled(0, 0, 0, 1.0);
            return true;
        }
        if (ControlifyBindings.NEXT_SLOT.on(controller).justPressed()) {
            component.mouseScrolled(0, 0, 0, -1.0);
            return true;
        }
        return false;
    }
}

