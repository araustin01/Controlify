package dev.isxander.controlify.compatibility.rso.mixins;

import java.util.List;
import java.util.Optional;
import me.flashyreese.mods.reeses_sodium_options.client.gui.frame.AbstractFrame;
import me.flashyreese.mods.reeses_sodium_options.client.gui.frame.tab.Tab;
import me.flashyreese.mods.reeses_sodium_options.client.gui.frame.tab.TabFrame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({TabFrame.class})
public interface TabFrameAccessor {
   @Accessor("tabs")
   List<Tab<?>> getTabs();

   @Accessor("selectedTab")
   Optional<Tab<?>> getSelectedTab();

   @Accessor("selectedFrame")
   AbstractFrame getSelectedFrame();
}
