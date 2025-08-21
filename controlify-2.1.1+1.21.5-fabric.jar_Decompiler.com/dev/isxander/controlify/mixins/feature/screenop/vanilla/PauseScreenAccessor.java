package dev.isxander.controlify.mixins.feature.screenop.vanilla;

import net.minecraft.class_433;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_433.class})
public interface PauseScreenAccessor {
   @Accessor("field_19319")
   boolean getShowPauseMenu();
}
