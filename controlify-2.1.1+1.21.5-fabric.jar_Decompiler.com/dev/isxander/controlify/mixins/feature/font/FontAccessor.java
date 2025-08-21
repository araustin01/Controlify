package dev.isxander.controlify.mixins.feature.font;

import net.minecraft.class_2960;
import net.minecraft.class_327;
import net.minecraft.class_377;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_327.class})
public interface FontAccessor {
   @Invoker("method_27526")
   class_377 invokeGetFontSet(class_2960 var1);
}
