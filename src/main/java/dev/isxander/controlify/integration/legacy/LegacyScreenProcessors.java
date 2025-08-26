package dev.isxander.controlify.integration.legacy;

import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.screenop.ScreenProcessorFactory;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.utils.CUtil;

// Direct imports for Legacy4J screens (no reflection needed)
import wily.legacy.client.screen.PlayGameScreen;
import wily.legacy.client.screen.LegacyCraftingScreen;

/**
 * Screen processors for Legacy4J custom screens.
 * Provides native controller navigation for Legacy4J-specific interfaces.
 * Uses direct API calls following the vanilla compat pattern.
 */
public class LegacyScreenProcessors {
    private static boolean registered = false;

    public static void register() {
        if (registered) return;
        registered = true;
        
        try {
            // Register component processors first so widgets get processors
            LegacyComponentProcessors.register();

            // Register screen processors for specific Legacy4J screen types that actually exist
            ScreenProcessorFactory.registerProvider(PlayGameScreen.class, LegacyPlayGameScreenProcessor::new);
            ScreenProcessorFactory.registerProvider(LegacyCraftingScreen.class, LegacyCraftingScreenProcessor::new);

            CUtil.LOGGER.log("[Legacy4J] Registered screen processors with direct API access");
        } catch (Throwable t) {
            CUtil.LOGGER.warn("[Legacy4J] Failed to register screen processors - Legacy4J may not be available", t);
        }
    }

    /**
     * Screen processor for Legacy4J Play Game Screen
     */
    public static class LegacyPlayGameScreenProcessor extends ScreenProcessor<PlayGameScreen> {
        public LegacyPlayGameScreenProcessor(PlayGameScreen screen) {
            super(screen);
        }

        @Override
        public void onControllerUpdate(ControllerEntity controller) {
            // Handle Legacy4J-specific bindings for play game screen
            if (LegacyApiFacade.TabListAccess.controlTab(screen,
                LegacyBindings.LEGACY_RECIPE_CYCLE_PREV.on(controller).justPressed(),
                LegacyBindings.LEGACY_RECIPE_CYCLE_NEXT.on(controller).justPressed())) {
                return;
            }

            // Call parent for standard processing
            super.onControllerUpdate(controller);
        }
    }

    /**
     * Screen processor for Legacy4J Crafting Screen
     */
    public static class LegacyCraftingScreenProcessor extends ScreenProcessor<LegacyCraftingScreen> {
        public LegacyCraftingScreenProcessor(LegacyCraftingScreen screen) {
            super(screen);
        }

        @Override
        public void onControllerUpdate(ControllerEntity controller) {
            // Handle info toggle binding if the screen supports it
            if (LegacyBindings.LEGACY_INFO_TOGGLE.on(controller).justPressed()) {
                // Use key simulation since there is no public API for toggleInfo
                screen.keyPressed(org.lwjgl.glfw.GLFW.GLFW_KEY_X, 0, 0);
                return;
            }

            // Call parent for standard processing
            super.onControllerUpdate(controller);
        }
    }
}
