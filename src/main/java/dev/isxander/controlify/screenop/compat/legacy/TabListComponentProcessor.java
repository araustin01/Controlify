package dev.isxander.controlify.screenop.compat.legacy;

import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.haptic.HapticEffects;
import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.utils.CUtil;
import wily.legacy.client.screen.TabList;

/**
 * Component processor for Legacy4J TabList components.
 * Provides controller navigation for tab switching functionality using direct API calls.
 */
public class TabListComponentProcessor implements ComponentProcessor {
    private final TabList tabList;

    public TabListComponentProcessor(TabList tabList) {
        this.tabList = tabList;
        CUtil.LOGGER.log("[LegacyCompat] Using TabListComponentProcessor for: " + tabList.getClass().getName());
    }

    @Override
    public boolean overrideControllerNavigation(ScreenProcessor<?> screen, ControllerEntity controller) {
        // Handle left/right bumper navigation for tabs using direct API
        if (ControlifyBindings.PREV_SLOT.on(controller).justPressed()) {
            boolean result = tabList.controlTab(true, false); // left = true
            if (result) {
                controller.hdHaptics().ifPresent(hh -> hh.playHaptic(HapticEffects.NAVIGATE));
            }
            return result;
        }

        if (ControlifyBindings.NEXT_SLOT.on(controller).justPressed()) {
            boolean result = tabList.controlTab(false, true); // right = true
            if (result) {
                controller.hdHaptics().ifPresent(hh -> hh.playHaptic(HapticEffects.NAVIGATE));
            }
            return result;
        }

        // Handle directional navigation within tabs
        if (ControlifyBindings.GUI_NAVI_LEFT.on(controller).justPressed()) {
            return tabList.controlTab(true, false);
        }

        if (ControlifyBindings.GUI_NAVI_RIGHT.on(controller).justPressed()) {
            return tabList.controlTab(false, true);
        }

        return false;
    }

    @Override
    public boolean overrideControllerButtons(ScreenProcessor<?> screen, ControllerEntity controller) {
        if (ControlifyBindings.GUI_PRESS.on(controller).justPressed()) {
            controller.hdHaptics().ifPresent(hh -> hh.playHaptic(HapticEffects.NAVIGATE));
            // TabList handles activation through its standard mouse/key events
            return tabList.mouseClicked(0, 0, 0);
        }
        return false;
    }
}
