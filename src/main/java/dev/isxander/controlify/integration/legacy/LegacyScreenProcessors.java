package dev.isxander.controlify.integration.legacy;

import dev.isxander.controlify.screenop.ScreenProcessorFactory;
import dev.isxander.controlify.screenop.compat.legacy.LegacyNoMouseScreenProcessor;
import dev.isxander.controlify.screenop.compat.legacy.LegacyScreenTabProcessor;
import dev.isxander.controlify.utils.CUtil;

// Direct imports for Legacy4J screens (no reflection needed)
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import wily.legacy.client.screen.*;
import dev.isxander.controlify.screenop.compat.legacy.LegacyScreenProcessor;

/**
 * Fresh Legacy4J compatibility wiring: all Legacy screens are handled by a single
 * LegacyScreenProcessor which delegates navigation to Legacy via key events.
 */
public class LegacyScreenProcessors {
    private static boolean registered = false;

    public static void register() {
        if (registered) return;
        registered = true;
        try {
            // Register a single generic processor for all known Legacy screens
            ScreenProcessorFactory.registerProvider(PlayGameScreen.class, LegacyScreenTabProcessor::new);
            ScreenProcessorFactory.registerProvider(PanelVListScreen.class, LegacyScreenTabProcessor::new);

            ScreenProcessorFactory.registerProvider(LegacyCraftingScreen.class, LegacyNoMouseScreenProcessor::new);
            ScreenProcessorFactory.registerProvider(LegacyLoomScreen.class, LegacyNoMouseScreenProcessor::new);
            ScreenProcessorFactory.registerProvider(LegacyMerchantScreen.class, LegacyNoMouseScreenProcessor::new);
            ScreenProcessorFactory.registerProvider(LegacyLoomScreen.class, LegacyNoMouseScreenProcessor::new);
            ScreenProcessorFactory.registerProvider(LegacyStonecutterScreen.class, LegacyNoMouseScreenProcessor::new);

            ScreenProcessorFactory.registerProvider(MixedCraftingScreen.class,  LegacyScreenTabProcessor::new);
            ScreenProcessorFactory.registerProvider(CreativeModeScreen.class, LegacyScreenTabProcessor::new);

            ScreenProcessorFactory.registerProvider(CreateWorldScreen.class, LegacyScreenProcessor::new);

            CUtil.LOGGER.log("[Legacy4J] Registered generic LegacyScreenProcessorss for Legacy screens");
        } catch (Throwable t) {
            CUtil.LOGGER.warn("[Legacy4J] Failed to register generic Legacy screen processor - Legacy4J may not be available", t);
        }
    }
}
