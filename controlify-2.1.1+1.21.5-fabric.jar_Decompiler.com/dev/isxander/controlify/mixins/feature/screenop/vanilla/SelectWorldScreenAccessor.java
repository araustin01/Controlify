package dev.isxander.controlify.mixins.feature.screenop.vanilla;

import net.minecraft.class_4185;
import net.minecraft.class_526;
import net.minecraft.class_528;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_526.class})
public interface SelectWorldScreenAccessor {
   @Accessor("field_3224")
   class_4185 getSelectButton();

   @Accessor("field_3218")
   class_528 getList();
}
