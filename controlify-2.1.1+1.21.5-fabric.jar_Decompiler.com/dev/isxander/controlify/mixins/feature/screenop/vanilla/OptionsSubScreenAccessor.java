package dev.isxander.controlify.mixins.feature.screenop.vanilla;

import net.minecraft.class_437;
import net.minecraft.class_4667;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_4667.class})
public interface OptionsSubScreenAccessor {
   @Accessor("field_21335")
   class_437 getLastScreen();
}
