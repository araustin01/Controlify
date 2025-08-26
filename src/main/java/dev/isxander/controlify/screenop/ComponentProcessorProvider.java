package dev.isxander.controlify.screenop;

import net.minecraft.client.gui.components.events.GuiEventListener;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.controlify.screenop.compat.legacy.LegacyGenericNavProcessor;

public interface ComponentProcessorProvider {
    ComponentProcessor componentProcessor();

    static ComponentProcessor provide(GuiEventListener component) {
        String fqcn = component.getClass().getName();
        boolean isLegacy = fqcn.startsWith("wily.legacy.");

        if (isLegacy) {
            var regOpt = REGISTRY.get(component);
            if (regOpt.isPresent()) {
                var proc = regOpt.get();
                CUtil.LOGGER.log("Overriding legacy-provided processor with registry: " + fqcn + " -> " + proc.getClass().getSimpleName());
                return proc;
            }
            CUtil.LOGGER.log("Using legacy generic fallback processor for: " + fqcn);
            return new LegacyGenericNavProcessor(component);
        }

        if (component instanceof ComponentProcessorProvider provider) {
            CUtil.LOGGER.log("Using provided processor for: " + fqcn);
            return provider.componentProcessor();
        }

        var opt = REGISTRY.get(component);
        if (opt.isPresent()) {
            var processor = opt.get();
            CUtil.LOGGER.log("Using registry processor for: " + fqcn + " -> " + processor.getClass().getSimpleName());
            return processor;
        }

        CUtil.LOGGER.log("Unknown provided processor (no fallback exists); returning empty for: " + component.getClass().getSimpleName());

        return ComponentProcessor.EMPTY;

    }

    Registry<GuiEventListener, ComponentProcessor> REGISTRY = new Registry<>();
}
