package dev.isxander.controlify.screenop;

import net.minecraft.client.gui.components.events.GuiEventListener;
import dev.isxander.controlify.utils.CUtil;

public interface ComponentProcessorProvider {
    ComponentProcessor componentProcessor();
    static ComponentProcessor provide(GuiEventListener component) {
        String fqcn = component.getClass().getName();

        var opt = REGISTRY.get(component);
        if (opt.isPresent()) {
            var processor = opt.get();
            CUtil.LOGGER.log("Using registry processor for: " + fqcn + " -> " + processor.getClass().getSimpleName());
            return processor;
        } else if (component instanceof ComponentProcessorProvider provider) {
            var proc = provider.componentProcessor();
            return proc;
        }

        // Final fallback
        CUtil.LOGGER.log("No processor found; returning empty for: " + component.getClass().getSimpleName());
        return ComponentProcessor.EMPTY;

    }

    Registry<GuiEventListener, ComponentProcessor> REGISTRY = new Registry<>();
}
