package dev.isxander.controlify.screenop.compat.legacy;

import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.utils.CUtil;
import org.lwjgl.glfw.GLFW;
import wily.legacy.client.screen.LegacySliderButton;

/**
 * Controlify override for Legacy4J's LegacySliderButton so slider input is handled by Controlify.
 * Uses left/right navigation bindings to adjust value.
 */
public class LegacySliderButtonComponentProcessor implements ComponentProcessor {
    private final LegacySliderButton<?> slider;

    private static final int SLIDER_CHANGE_DELAY = 1;
    private int lastSliderChange = SLIDER_CHANGE_DELAY;

    public LegacySliderButtonComponentProcessor(LegacySliderButton<?> slider) {
        this.slider = slider;
        CUtil.LOGGER.log("[LegacyCompat] Using LegacySliderButtonComponentProcessor for: " + slider.getClass().getName());
    }

    @Override
    public boolean overrideControllerNavigation(ScreenProcessor<?> screen, ControllerEntity controller) {
        boolean canSliderChange = ++lastSliderChange > SLIDER_CHANGE_DELAY;

        if (ControlifyBindings.GUI_NAVI_RIGHT.on(controller).digitalNow()) {
            if (canSliderChange) {
                slider.keyPressed(GLFW.GLFW_KEY_RIGHT, 0, 0);
                lastSliderChange = 0;
            }
            return true;
        }
        if (ControlifyBindings.GUI_NAVI_LEFT.on(controller).digitalNow()) {
            if (canSliderChange) {
                slider.keyPressed(GLFW.GLFW_KEY_LEFT, 0, 0);
                lastSliderChange = 0;
            }
            return true;
        }
        return false;
    }
}
