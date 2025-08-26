package dev.isxander.controlify.screenop.compat.legacy;

import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.haptic.HapticEffects;
import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.integration.legacy.LegacyBindings;
import dev.isxander.controlify.utils.CUtil;
import org.lwjgl.glfw.GLFW;
import wily.legacy.client.screen.RecipeIconHolder;

/**
 * Component processor for Legacy4J RecipeIconHolder components.
 * Provides controller navigation for recipe cycling using key simulation fallbacks.
 */
public class RecipeIconHolderComponentProcessor implements ComponentProcessor {
    private final RecipeIconHolder recipeHolder;

    public RecipeIconHolderComponentProcessor(RecipeIconHolder recipeHolder) {
        this.recipeHolder = recipeHolder;
        CUtil.LOGGER.log("[LegacyCompat] Using RecipeIconHolderComponentProcessor for: " + recipeHolder.getClass().getName());
    }

    @Override
    public boolean overrideControllerNavigation(ScreenProcessor<?> screen, ControllerEntity controller) {
        // Handle recipe cycling with dedicated bindings using key simulation
        if (LegacyBindings.LEGACY_RECIPE_CYCLE_NEXT.on(controller).justPressed()) {
            recipeHolder.keyPressed(GLFW.GLFW_KEY_RIGHT, 0, 0);
            controller.hdHaptics().ifPresent(hh -> hh.playHaptic(HapticEffects.NAVIGATE));
            return true;
        }

        if (LegacyBindings.LEGACY_RECIPE_CYCLE_PREV.on(controller).justPressed()) {
            recipeHolder.keyPressed(GLFW.GLFW_KEY_LEFT, 0, 0);
            controller.hdHaptics().ifPresent(hh -> hh.playHaptic(HapticEffects.NAVIGATE));
            return true;
        }

        // Also handle with shoulder buttons for convenience
        if (ControlifyBindings.PREV_SLOT.on(controller).justPressed()) {
            recipeHolder.keyPressed(GLFW.GLFW_KEY_LEFT, 0, 0);
            controller.hdHaptics().ifPresent(hh -> hh.playHaptic(HapticEffects.NAVIGATE));
            return true;
        }

        if (ControlifyBindings.NEXT_SLOT.on(controller).justPressed()) {
            recipeHolder.keyPressed(GLFW.GLFW_KEY_RIGHT, 0, 0);
            controller.hdHaptics().ifPresent(hh -> hh.playHaptic(HapticEffects.NAVIGATE));
            return true;
        }

        return false;
    }

    @Override
    public boolean overrideControllerButtons(ScreenProcessor<?> screen, ControllerEntity controller) {
        if (ControlifyBindings.GUI_PRESS.on(controller).justPressed()) {
            controller.hdHaptics().ifPresent(hh -> hh.playHaptic(HapticEffects.NAVIGATE));
            return recipeHolder.mouseClicked(0, 0, 0);
        }
        return false;
    }
}
