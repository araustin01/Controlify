package dev.isxander.controlify.platform.fabric.mixins;

import net.minecraft.class_1761;
import net.minecraft.class_481;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_481.class})
public interface CreativeModeInventoryScreenAccessor {
   @Accessor("field_2896")
   static class_1761 getSelectedTab() {
      throw new AssertionError();
   }

   @Invoker("method_2466")
   void invokeSelectTab(class_1761 var1);
}
