package dev.isxander.controlify.screenop;

import net.minecraft.client.gui.components.events.GuiEventListener;
import dev.isxander.controlify.utils.CUtil;

public interface ComponentProcessorProvider {
    ComponentProcessor componentProcessor();

    static ComponentProcessor provide(GuiEventListener component) {
        if (component instanceof ComponentProcessorProvider provider) {
            CUtil.LOGGER.log("Using provided processor for: " + component.getClass().getName());
            return provider.componentProcessor();
        }

        var opt = REGISTRY.get(component);
        if (opt.isPresent()) return opt.get();

        CUtil.LOGGER.log("Unknown provided processor (no fallback exists); returning empty for: " + component.getClass().getSimpleName());

        return ComponentProcessor.EMPTY;

    }

    Registry<GuiEventListener, ComponentProcessor> REGISTRY = new Registry<>();
}
