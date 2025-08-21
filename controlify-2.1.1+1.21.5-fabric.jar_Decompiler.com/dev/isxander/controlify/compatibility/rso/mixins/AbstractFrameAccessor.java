package dev.isxander.controlify.compatibility.rso.mixins;

import java.util.List;
import me.flashyreese.mods.reeses_sodium_options.client.gui.frame.AbstractFrame;
import net.caffeinemc.mods.sodium.client.gui.options.control.ControlElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({AbstractFrame.class})
public interface AbstractFrameAccessor {
   @Accessor("controlElements")
   List<ControlElement<?>> getControlElements();
}
