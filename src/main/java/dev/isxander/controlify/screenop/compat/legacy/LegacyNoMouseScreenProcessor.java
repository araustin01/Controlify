package dev.isxander.controlify.screenop.compat.legacy;

import dev.isxander.controlify.virtualmouse.VirtualMouseBehaviour;
import net.minecraft.client.gui.screens.Screen;

public class LegacyNoMouseScreenProcessor<T extends Screen> extends LegacyScreenTabProcessor<T> {
    public LegacyNoMouseScreenProcessor(T screen) {
        super(screen);
    }

    @Override
    public VirtualMouseBehaviour virtualMouseBehaviour() {
        return VirtualMouseBehaviour.DISABLED;
    }
}
