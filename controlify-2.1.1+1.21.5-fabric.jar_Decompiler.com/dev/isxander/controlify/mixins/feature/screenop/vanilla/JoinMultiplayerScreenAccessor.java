package dev.isxander.controlify.mixins.feature.screenop.vanilla;

import net.minecraft.class_4185;
import net.minecraft.class_500;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_500.class})
public interface JoinMultiplayerScreenAccessor {
   @Accessor("field_3050")
   class_4185 getSelectButton();
}
