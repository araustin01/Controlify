package dev.isxander.controlify.integration.legacy;

import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ComponentProcessorProvider;
import dev.isxander.controlify.screenop.compat.legacy.*;
import dev.isxander.controlify.utils.CUtil;

// Direct imports for Legacy4J components that implement GuiEventListener
import wily.legacy.client.screen.TabList;
import wily.legacy.client.screen.LegacyTabButton;

/**
 * Component processors for Legacy4J UI elements.
 * Provides fine-grained controller navigation for specific Legacy4J components.
 * Uses direct API calls following the vanilla compat pattern.
 * Only registers components that properly implement GuiEventListener.
 */
public class LegacyComponentProcessors {
    
    /**
     * Register component processors for Legacy4J components with the global registry.
     * This follows the same pattern as vanilla component registration.
     */
    public static void register() {
        try {
            // Register processors for Legacy4J components that implement GuiEventListener
            ComponentProcessorProvider.REGISTRY.register(TabList.class, TabListComponentProcessor::new);
            ComponentProcessorProvider.REGISTRY.register(LegacyTabButton.class, LegacyTabButtonComponentProcessor::new);

            // Note: LegacyScroller, RenderableVList, and RecipeIconHolder do not implement GuiEventListener
            // so they cannot be registered with the component processor registry

            CUtil.LOGGER.log("[Legacy4J] Registered component processors with direct API access");
        } catch (Throwable t) {
            CUtil.LOGGER.warn("[Legacy4J] Failed to register component processors - Legacy4J may not be available", t);
        }
    }
}
