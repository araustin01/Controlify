package dev.isxander.controlify.mixins.feature.virtualmouse;

import net.minecraft.class_312;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_312.class})
public interface MouseHandlerAccessor {
   @Invoker("method_1600")
   void invokeOnMove(long var1, double var3, double var5);

   @Invoker("method_1601")
   void invokeOnPress(long var1, int var3, int var4, int var5);

   @Invoker("method_1598")
   void invokeOnScroll(long var1, double var3, double var5);

   @Accessor("field_1783")
   void setMouseGrabbed(boolean var1);
}
