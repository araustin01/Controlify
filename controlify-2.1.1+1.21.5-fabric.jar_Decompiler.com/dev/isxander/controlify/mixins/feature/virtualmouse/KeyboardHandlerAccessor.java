package dev.isxander.controlify.mixins.feature.virtualmouse;

import net.minecraft.class_309;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_309.class})
public interface KeyboardHandlerAccessor {
   @Invoker("method_1466")
   void invokeKeyPress(long var1, int var3, int var4, int var5, int var6);
}
