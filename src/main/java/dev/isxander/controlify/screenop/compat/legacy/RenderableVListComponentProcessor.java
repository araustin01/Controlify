package dev.isxander.controlify.screenop.compat.legacy;

import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.haptic.HapticEffects;
import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.utils.CUtil;
import org.lwjgl.glfw.GLFW;
import wily.legacy.client.screen.RenderableVList;

/**
 * Component processor for Legacy4J RenderableVList components.
 * Provides controller navigation for vertical list navigation using correct method signatures.
 */
public class RenderableVListComponentProcessor implements ComponentProcessor {
    private final RenderableVList list;

    public RenderableVListComponentProcessor(RenderableVList list) {
        this.list = list;
        CUtil.LOGGER.log("[LegacyCompat] Using RenderableVListComponentProcessor for: " + list.getClass().getName());
    }

    @Override
    public boolean overrideControllerNavigation(ScreenProcessor<?> screen, ControllerEntity controller) {
        // Handle vertical navigation using the correct keyPressed(int) method signature
        if (ControlifyBindings.GUI_NAVI_UP.on(controller).justPressed()) {
            list.keyPressed(GLFW.GLFW_KEY_UP);
            controller.hdHaptics().ifPresent(hh -> hh.playHaptic(HapticEffects.NAVIGATE));
            return true;
        }

        if (ControlifyBindings.GUI_NAVI_DOWN.on(controller).justPressed()) {
            list.keyPressed(GLFW.GLFW_KEY_DOWN);
            controller.hdHaptics().ifPresent(hh -> hh.playHaptic(HapticEffects.NAVIGATE));
            return true;
        }

        // Handle page navigation with shoulder buttons using page up/down keys
        if (ControlifyBindings.PREV_SLOT.on(controller).justPressed()) {
            list.keyPressed(GLFW.GLFW_KEY_PAGE_UP);
            controller.hdHaptics().ifPresent(hh -> hh.playHaptic(HapticEffects.NAVIGATE));
            return true;
        }

        if (ControlifyBindings.NEXT_SLOT.on(controller).justPressed()) {
            list.keyPressed(GLFW.GLFW_KEY_PAGE_DOWN);
            controller.hdHaptics().ifPresent(hh -> hh.playHaptic(HapticEffects.NAVIGATE));
            return true;
        }

        return false;
    }

    @Override
    public boolean overrideControllerButtons(ScreenProcessor<?> screen, ControllerEntity controller) {
        if (ControlifyBindings.GUI_PRESS.on(controller).justPressed()) {
            controller.hdHaptics().ifPresent(hh -> hh.playHaptic(HapticEffects.NAVIGATE));
            return list.keyPressed(GLFW.GLFW_KEY_ENTER);
        }
        return false;
    }
}
