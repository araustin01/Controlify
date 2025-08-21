package dev.isxander.splitscreen.client.mixins.core;

import net.minecraft.class_1041;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_1041.class})
public interface WindowAccessor {
   @Invoker("method_4483")
   void callRefreshFramebufferSize();
}
