package dev.isxander.controlify.mixins.feature.ui;

import net.minecraft.class_2960;
import net.minecraft.class_350;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_350.class})
public interface AbstractSelectionListAccessor {
   @Accessor("field_49478")
   static class_2960 getMenuListBackground() {
      throw new AssertionError();
   }

   @Accessor("field_49892")
   static class_2960 getInWorldMenuListBackground() {
      throw new AssertionError();
   }
}
