package dev.isxander.controlify.integration.legacy;

import dev.isxander.controlify.api.bind.ControlifyBindApi;
import dev.isxander.controlify.api.bind.InputBindingSupplier;
import dev.isxander.controlify.bindings.BindContext;
import net.minecraft.network.chat.Component;

/**
 * Legacy4J-specific input bindings for Controlify integration.
 * Provides dedicated bindings for Legacy4J UI interactions like the info toggle.
 */
public class LegacyBindings {
    
    public static final Component LEGACY_CATEGORY = Component.translatable("controlify.binding_category.legacy4j");

    /** 
     * Legacy4J info toggle binding (typically X button in crafting screens).
     * Used to cycle through different information views in Legacy4J interfaces.
     */
    public static final InputBindingSupplier LEGACY_INFO_TOGGLE = ControlifyBindApi.get().registerBinding(builder -> builder
            .id("controlify", "legacy_info_toggle")
            .category(LEGACY_CATEGORY)
            .allowedContexts(BindContext.REGULAR_SCREEN)
    );

    /**
     * Legacy4J recipe cycle binding for quick recipe navigation.
     * Used in crafting interfaces to cycle through available recipes.
     */
    public static final InputBindingSupplier LEGACY_RECIPE_CYCLE_NEXT = ControlifyBindApi.get().registerBinding(builder -> builder
            .id("controlify", "legacy_recipe_cycle_next")
            .category(LEGACY_CATEGORY)
            .allowedContexts(BindContext.REGULAR_SCREEN)
    );

    public static final InputBindingSupplier LEGACY_RECIPE_CYCLE_PREV = ControlifyBindApi.get().registerBinding(builder -> builder
            .id("controlify", "legacy_recipe_cycle_prev")
            .category(LEGACY_CATEGORY)
            .allowedContexts(BindContext.REGULAR_SCREEN)
    );

    /**
     * Legacy4J container interaction bindings for special actions.
     */
    public static final InputBindingSupplier LEGACY_CONTAINER_ACTION = ControlifyBindApi.get().registerBinding(builder -> builder
            .id("controlify", "legacy_container_action")
            .category(LEGACY_CATEGORY)
            .allowedContexts(BindContext.CONTAINER, BindContext.REGULAR_SCREEN)
    );

    /**
     * Register all Legacy4J bindings. Should be called during mod initialization.
     */
    public static void register() {
        // Bindings are registered via the static field initialization above
        // This method is provided for explicit registration calls if needed
    }
}
