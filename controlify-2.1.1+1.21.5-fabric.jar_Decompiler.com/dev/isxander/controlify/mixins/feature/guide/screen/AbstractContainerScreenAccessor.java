package dev.isxander.controlify.mixins.feature.guide.screen;

import net.minecraft.class_1735;
import net.minecraft.class_465;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_465.class})
public interface AbstractContainerScreenAccessor {
   @Accessor("field_2787")
   class_1735 getHoveredSlot();

   @Invoker("method_2381")
   boolean invokeHasClickedOutside(double var1, double var3, int var5, int var6, int var7);

   @Accessor("field_2776")
   int getLeftPos();

   @Accessor("field_2800")
   int getTopPos();
}
