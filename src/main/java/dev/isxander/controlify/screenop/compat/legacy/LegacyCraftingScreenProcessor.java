package dev.isxander.controlify.screenop.compat.legacy;

import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.virtualmouse.VirtualMouseBehaviour;
import net.minecraft.client.gui.screens.Screen;
import org.lwjgl.glfw.GLFW;

/**
 * Generic processor for Legacy4J screens: delegates navigation to Legacy via keyPressed events.
 * This avoids double-handling (Controlify focus navigation + Legacy handling).
 */
public class LegacyCraftingScreenProcessor<T extends Screen> extends LegacyScreenTabProcessor<T> {
    public LegacyCraftingScreenProcessor(T screen) {
        super(screen);
    }

    @Override
    public VirtualMouseBehaviour virtualMouseBehaviour() {
        return VirtualMouseBehaviour.DISABLED;
    }
}
