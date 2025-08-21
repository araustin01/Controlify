package dev.isxander.controlify.compatibility.sodium.mixins;

import net.caffeinemc.mods.sodium.client.gui.widgets.FlatButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({FlatButtonWidget.class})
public interface FlatButtonWidgetAccessor {
   @Invoker("doAction")
   void invokeDoAction();
}
