package dev.isxander.controlify.screenop;

import net.minecraft.client.gui.components.events.GuiEventListener;
import dev.isxander.controlify.utils.CUtil;

public interface ComponentProcessorProvider {
    ComponentProcessor componentProcessor();
    static ComponentProcessor provide(GuiEventListener component) {
        var opt = REGISTRY.get(component);
        if (opt.isPresent()) {
            var processor = opt.get();
            return processor;
        } else if (component instanceof ComponentProcessorProvider provider) {
            var proc = provider.componentProcessor();
            return proc;
        }

        return ComponentProcessor.EMPTY;
    }

    Registry<GuiEventListener, ComponentProcessor> REGISTRY = new Registry<>();
}
