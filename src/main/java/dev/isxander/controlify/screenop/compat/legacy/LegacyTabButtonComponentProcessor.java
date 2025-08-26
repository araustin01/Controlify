package dev.isxander.controlify.screenop.compat.legacy;

import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.haptic.HapticEffects;
import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.utils.CUtil;
import net.minecraft.client.Minecraft;
import wily.legacy.client.screen.LegacyTabButton;

/**
 * Component processor for Legacy4J LegacyTabButton components.
 * Provides controller navigation for tab button interactions using direct API calls.
 */
public class LegacyTabButtonComponentProcessor implements ComponentProcessor {
    private final LegacyTabButton tabButton;

    public LegacyTabButtonComponentProcessor(LegacyTabButton tabButton) {
        this.tabButton = tabButton;
        CUtil.LOGGER.log("[LegacyCompat] Using LegacyTabButtonComponentProcessor for: " + tabButton.getClass().getName());
    }

    @Override
    public boolean overrideControllerButtons(ScreenProcessor<?> screen, ControllerEntity controller) {
        if (ControlifyBindings.GUI_PRESS.on(controller).justPressed()) {
            controller.hdHaptics().ifPresent(hh -> hh.playHaptic(HapticEffects.NAVIGATE));
            tabButton.playDownSound(Minecraft.getInstance().getSoundManager());
            tabButton.onPress();
            return true;
        }
        return false;
    }
}
