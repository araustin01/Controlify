package dev.isxander.controlify.screenop.compat.legacy;

import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.haptic.HapticEffects;
import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.utils.CUtil;
import wily.legacy.client.screen.LegacyScroller;

/**
 * Component processor for Legacy4J LegacyScroller components.
 * Provides controller navigation for scrollable UI elements using compatible method calls.
 */
public class LegacyScrollerComponentProcessor implements ComponentProcessor {
    private final LegacyScroller scroller;

    public LegacyScrollerComponentProcessor(LegacyScroller scroller) {
        this.scroller = scroller;
        CUtil.LOGGER.log("[LegacyCompat] Using LegacyScrollerComponentProcessor for: " + scroller.getClass().getName());
    }

    @Override
    public boolean overrideControllerNavigation(ScreenProcessor<?> screen, ControllerEntity controller) {
        // Handle scroll navigation using mouse scroll events (LegacyScroller only takes one double parameter)
        if (ControlifyBindings.GUI_NAVI_UP.on(controller).justPressed()) {
            scroller.mouseScrolled(1.0);
            controller.hdHaptics().ifPresent(hh -> hh.playHaptic(HapticEffects.NAVIGATE));
            return true;
        }

        if (ControlifyBindings.GUI_NAVI_DOWN.on(controller).justPressed()) {
            scroller.mouseScrolled(-1.0);
            controller.hdHaptics().ifPresent(hh -> hh.playHaptic(HapticEffects.NAVIGATE));
            return true;
        }

        // Handle shoulder button scrolling
        if (ControlifyBindings.PREV_SLOT.on(controller).justPressed()) {
            scroller.mouseScrolled(1.0);
            controller.hdHaptics().ifPresent(hh -> hh.playHaptic(HapticEffects.NAVIGATE));
            return true;
        }

        if (ControlifyBindings.NEXT_SLOT.on(controller).justPressed()) {
            scroller.mouseScrolled(-1.0);
            controller.hdHaptics().ifPresent(hh -> hh.playHaptic(HapticEffects.NAVIGATE));
            return true;
        }

        return false;
    }

    @Override
    public boolean overrideControllerButtons(ScreenProcessor<?> screen, ControllerEntity controller) {
        if (ControlifyBindings.GUI_PRESS.on(controller).justPressed()) {
            controller.hdHaptics().ifPresent(hh -> hh.playHaptic(HapticEffects.NAVIGATE));
            return scroller.mouseClicked(0, 0, 0);
        }
        return false;
    }
}
